package com.finiteloop.musica;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.finiteloop.musica.Models.PostModel;
import com.finiteloop.musica.NetworkUtils.MusicaServerAPICalls;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;

public class PlaylistActivity extends AppCompatActivity {

    public ArrayList<PostModel> mPlayListArrayList;
    MediaPlayer mMediaPlayer;
    Context con;
    TextView mNumOfSongs;
    FloatingActionButton mShareButton;
    private RecyclerView recyclerView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        mNumOfSongs = (TextView) findViewById(R.id.activity_playlist_num_of_songs);
        mShareButton = (FloatingActionButton) findViewById(R.id.activity_playlist_share_button);
        recyclerView = (RecyclerView) findViewById(R.id.activity_playlist_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mPlayListArrayList = new ArrayList<>();
        //recyclerView.setAdapter(new RecyclerViewAdapter(getApplicationContext(),mPlayListArrayList));

        toolbar = (Toolbar) findViewById(R.id.activity_playlist_toolBar);
        mMediaPlayer = MediaPlayer.create(this, R.raw.coldplay);

        con = this;

        mNumOfSongs.setText("0 songs");


        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        }

        new MusicaServerAPICalls() {
            @Override
            public void isRequestSuccessful(boolean isSuccessful, String message) {
                if (isSuccessful) {
                    try {
                        mPlayListArrayList = parseJsonResponse(message);
                        mNumOfSongs.setText(mPlayListArrayList.size() + " songs");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    recyclerView.setAdapter(new RecyclerViewAdapter(getBaseContext(), mPlayListArrayList));
                } else {
                    Toast.makeText(getApplicationContext(), "There was an error while fetching posts!", Toast.LENGTH_SHORT).show();
                }
            }
        }.getUserPrivatePosts(getApplicationContext());

    }

    // json parsing of the getHomeStreamPostOfUser Api call
    public ArrayList<PostModel> parseJsonResponse(String response) throws JSONException {
        ArrayList<PostModel> arrayList = new ArrayList<>();

        JSONArray data = new JSONArray(response);
        for (int i = 0; i < data.length(); i++) {
            PostModel postModel = new PostModel();
            JSONObject post = data.getJSONObject(i);
            JSONArray no_of_likes = post.getJSONArray("user_like");
            JSONArray no_of_loves = post.getJSONArray("user_love");

            postModel.setPost_id(post.getString("_id"));
            postModel.setGenreTag(post.getString("post_genre_tag"));
            postModel.setTitle(post.getString("post_title"));
            postModel.setNo_of_likes(no_of_likes.length() + "");
            postModel.setNo_of_loves(no_of_loves.length() + "");
            postModel.setPost_pic_url(post.getString("post_album_pic"));
            postModel.setUser_profile_pic(post.getString("user_profile_pic"));
            postModel.setUsername(post.getString("username"));
            postModel.setPostURL(post.getString("post_song_url"));
            postModel.setTimeStamp(Timestamp.valueOf(post.getString("post_time_stamp")));
            postModel.setPrivatePost(post.getBoolean("private_post"));
            postModel.setHits(post.getInt("hits"));
            postModel.setUser_email(post.getString("email_address"));
            arrayList.add(postModel);
        }
        return arrayList;
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

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
        ArrayList<PostModel> mPosts = new ArrayList<>();
        Context mContext;

        public RecyclerViewAdapter(Context context, ArrayList<PostModel> p) {
            mPosts = p;
            mContext = context;
        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.playlist_card_view, parent, false);
            return new RecyclerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            holder.mTitleTextView.setText(mPosts.get(position).getTitle());
            Picasso.with(mContext).load(Uri.parse(mPosts.get(position).getPost_pic_url())).into(holder.mCoverImageView);
            holder.bindData(mPosts.get(position).getPostURL(), mPosts.get(position).getPost_pic_url(), mPosts.get(position).getTitle());
        }

        @Override
        public int getItemCount() {
            return mPosts.size();
        }
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTitleTextView;
        ImageView mCoverImageView;
        String mMusicURL, mCoverURL, mMusicTitle;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.activity_playlist_recycler_view_music_title);
            mCoverImageView = (ImageView) itemView.findViewById(R.id.activity_playlist_cover_image);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(con, MusicPlayer.class);
            i.putExtra("Song URL", mMusicURL);
            i.putExtra("Cover URL", mCoverURL);
            i.putExtra("Title", mMusicTitle);
            startActivity(i);
        }

        public void bindData(String musicURL, String coverURL, String title) {
            mMusicURL = musicURL;
            mCoverURL = coverURL;
            mMusicTitle = title;
        }
    }
}
