package com.finiteloop.musica;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.finiteloop.musica.Models.PostModel;
import com.finiteloop.musica.NetworkUtils.MusicaServerAPICalls;
import com.finiteloop.musica.SharedPreferencesUtils.UserDataSharedPreference;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    ImageView mCoverPic;
    RecyclerView mRecyclerView;
    Toolbar mToolbar;
    CollapsingToolbarLayout mCollapsingToolbar;
    ToggleButton mFollowButton;
    TextView mProfileName;
    CircularImageView mProfilePic;
    String mUsername;
    String mProfilePicUrl;
    String mEmailId;
    TextView mNumOfSongsText;
    ArrayList<PostModel> mProfilePlaylist;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mUsername = getIntent().getStringExtra("Username");
        mProfilePicUrl = getIntent().getStringExtra("Profile Pic");
        mEmailId = getIntent().getStringExtra("Email Id");
        // Log.d("Profile Username",mUsername);

        mCoverPic = (ImageView) findViewById(R.id.profile_activity_cover_pic);
        mRecyclerView = (RecyclerView) findViewById(R.id.profile_activity_recycler_view);
        mToolbar = (Toolbar) findViewById(R.id.profile_activity_toolbar);
        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.profile_activity_collapsing_toolbar);
        mFollowButton = (ToggleButton) findViewById(R.id.activity_profile_follow_button);
        mProfileName = (TextView) findViewById(R.id.profile_activity_profile_name);
        mProfilePic = (CircularImageView) findViewById(R.id.profile_activity_profile_pic);
        mNumOfSongsText = (TextView) findViewById(R.id.activity_profile_number_of_songs);

        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(Boolean.TRUE);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        }

        context = this;

        /*Drawable coverPic = getResources().getDrawable(R.drawable.concert);
        coverPic.setAlpha(150);
        mCoverPic.setImageDrawable(coverPic);*/

        mRecyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(), 2));

        mProfilePlaylist = new ArrayList<>();

       /* profileAlbum.add(new ProfileAlbums("Coldplay", 4, getResources().getDrawable(R.drawable.mountain_pic2)));
        profileAlbum.add(new ProfileAlbums("Florida Whistle", 1, getResources().getDrawable(R.drawable.mountain_pic2)));
        profileAlbum.add(new ProfileAlbums("One Direction", 2, getResources().getDrawable(R.drawable.mountain_pic2)));
        profileAlbum.add(new ProfileAlbums("Gangnam Style", 5, getResources().getDrawable(R.drawable.mountain_pic2)));
        profileAlbum.add(new ProfileAlbums("Arijit Singh", 25, getResources().getDrawable(R.drawable.mountain_pic2)));*/


        //mRecyclerView.setAdapter(new RecyclerViewAdapter(getBaseContext(), profileAlbum));

        mCollapsingToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this, ProfileDescriptionActivity.class);
                i.putExtra("Username", mUsername);
                i.putExtra("Profile Pic", mProfilePicUrl);
                startActivity(i);
            }
        });

        //Todo
        mFollowButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                } else {

                }
            }
        });


        new MusicaServerAPICalls() {
            @Override
            public void isRequestSuccessful(boolean isSuccessful, String message) {
                if (isSuccessful) {
                    try {
                        mProfilePlaylist = parseJsonResponse(message);
                        mNumOfSongsText.setText(mProfilePlaylist.size() + " Songs");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mRecyclerView.setAdapter(new RecyclerViewAdapter(getBaseContext(), mProfilePlaylist));
                } else {
                    Toast.makeText(getApplicationContext(), "There was an error while fetching posts!", Toast.LENGTH_SHORT).show();
                }
            }
        }.getUserPosts(getApplicationContext(), mEmailId);


    }

    private ArrayList<PostModel> parseJsonResponse(String response) throws JSONException {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_activity_album_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;

            case R.id.search:
                startActivity(new Intent(ProfileActivity.this, SearchActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mUsername.equals(UserDataSharedPreference.getUsername(getBaseContext()))) {
            mFollowButton.setVisibility(View.VISIBLE);
        } else {
            mFollowButton.setVisibility(View.GONE);
        }

        mProfileName.setText(mUsername);

        Picasso.with(getBaseContext()).load(Uri.parse(mProfilePicUrl)).into(mProfilePic);
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

        Context mContext;
        ArrayList<PostModel> mProfileAlbum;

        public RecyclerViewAdapter(Context context, ArrayList<PostModel> profileAlbum) {
            this.mContext = context;
            this.mProfileAlbum = profileAlbum;
        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getBaseContext()).inflate(getResources().getLayout(R.layout.activity_profile_card_item), parent, false);
            return new RecyclerViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            PostModel temp = mProfileAlbum.get(position);
            holder.mAlbumName.setText(temp.getTitle());
            Picasso.with(mContext).load(Uri.parse(temp.getPost_pic_url())).into(holder.mAlbumPic);
            holder.bindData(mProfileAlbum.get(position).getPostURL(), mProfileAlbum.get(position).getPost_pic_url(), mProfileAlbum.get(position).getTitle());
        }

        @Override
        public int getItemCount() {
            return this.mProfileAlbum.size();
        }
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mAlbumPic;
        TextView mAlbumName;
        String mMusicURL, mCoverURL, mMusicTitle;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mAlbumPic = (ImageView) itemView.findViewById(R.id.activity_profile_card_item_album_pic);
            mAlbumName = (TextView) itemView.findViewById(R.id.activity_profile_card_item_album_name);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(context, MusicPlayer.class);
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
