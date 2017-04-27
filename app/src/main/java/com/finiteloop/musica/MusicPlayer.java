package com.finiteloop.musica;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.finiteloop.musica.MusicPlayerUtils.MusicPlayerSingleton;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class MusicPlayer extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    Toolbar mToolbar;
    MediaPlayer mMediaPlayer;
    String mSong;
    String mCover;
    String mTitle;
    VideoView mVideoView;
    CircularImageView mAlbumPic;
    MediaController mediaController;
    TextView mAlbumTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        mToolbar = (Toolbar) findViewById(R.id.activity_music_player_toolbar);

        mCover = getIntent().getStringExtra("Cover URL");
        mSong = getIntent().getStringExtra("Song URL");
        mTitle = getIntent().getStringExtra("Title");

        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        }

        mAlbumPic = (CircularImageView) findViewById(R.id.activity_music_player_album_picture);
        mAlbumTitle = (TextView) findViewById(R.id.activity_music_player_album_name);

        mMediaPlayer = MusicPlayerSingleton.getInstance();
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mVideoView = (VideoView) findViewById(R.id.gif_loader);

        mediaController = new MediaController(this);
        mediaController.setAnchorView(mVideoView);

        try {
            mMediaPlayer.setDataSource(mSong);
            mMediaPlayer.setOnPreparedListener(MusicPlayer.this);
            mMediaPlayer.prepareAsync();
            String uriPath = "android.resource://com.finiteloop.musica/raw/giphy";
            Uri uri = Uri.parse(uriPath);
            mVideoView.setMediaController(null);
            mVideoView.setVideoURI(uri);
            mVideoView.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                mMediaPlayer.reset();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.reset();
        finish();
        startActivity(new Intent(MusicPlayer.this, HomeStreamActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Picasso.with(getApplicationContext()).load(Uri.parse(mCover)).into(mAlbumPic);
        mAlbumTitle.setText(mTitle);
    }

    @Override
    public void onBackPressed() {
        mMediaPlayer.reset();
        finish();
    }
}
