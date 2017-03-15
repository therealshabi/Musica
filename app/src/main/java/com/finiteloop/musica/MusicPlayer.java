package com.finiteloop.musica;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.concurrent.TimeUnit;

public class MusicPlayer extends AppCompatActivity {

    Toolbar mToolbar;
    MediaPlayer mMediaPlayer;
    TextView mCurrentTimeText;
    TextView mEndTimeText;
    ToggleButton mPlay;
    SeekBar mProgressSeek;
    double mCurrentTime = 0.0;
    double mEndTime = 0.0;
    Handler myHandler = new Handler();
    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            mCurrentTime = mMediaPlayer.getCurrentPosition();

            long mins = TimeUnit.MILLISECONDS.toMinutes((long) mCurrentTime);
            long secs = TimeUnit.MILLISECONDS.toSeconds((long) mCurrentTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) mCurrentTime));

            mProgressSeek.setProgress((int) mCurrentTime);
            myHandler.postDelayed(this, 100);
            mCurrentTimeText.setText(String.format("%02d", mins) + ":" + String.format("%02d", secs));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        mToolbar = (Toolbar) findViewById(R.id.activity_music_player_toolbar);

        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        }

        mMediaPlayer = MediaPlayer.create(this, R.raw.coldplay);
        mProgressSeek = (SeekBar) findViewById(R.id.activity_music_player_song_progress);
        mPlay = (ToggleButton) findViewById(R.id.activity_music_player_play_pause_button);
        mCurrentTimeText = (TextView) findViewById(R.id.activity_music_player_start_time_text);
        mEndTimeText = (TextView) findViewById(R.id.activity_music_player_end_time_text);

        mPlay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mMediaPlayer.start();
                    mCurrentTime = mMediaPlayer.getCurrentPosition();
                    mEndTime = mMediaPlayer.getDuration();
                    //Max Progress
                    mProgressSeek.setMax((int) mEndTime);
                    mProgressSeek.setProgress((int) mCurrentTime);
                    myHandler.postDelayed(UpdateSongTime, 100);

                    long mins = TimeUnit.MILLISECONDS.toMinutes((long) mEndTime);
                    long secs = TimeUnit.MILLISECONDS.toSeconds((long) mEndTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) mEndTime));
                    mEndTimeText.setText(String.format("%02d", mins) + ":" + String.format("%02d", secs));
                } else {
                    mMediaPlayer.pause();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
