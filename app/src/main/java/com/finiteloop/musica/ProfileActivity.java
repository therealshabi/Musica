package com.finiteloop.musica;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.finiteloop.musica.Models.ProfileAlbums;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    ImageView mCoverPic;
    RecyclerView mRecyclerView;
    Toolbar mToolbar;
    CollapsingToolbarLayout mCollapsingToolbar;
    ToggleButton mFollowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mCoverPic = (ImageView) findViewById(R.id.profile_activity_cover_pic);
        mRecyclerView = (RecyclerView) findViewById(R.id.profile_activity_recycler_view);
        mToolbar = (Toolbar) findViewById(R.id.profile_activity_toolbar);
        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.profile_activity_collapsing_toolbar);
        mFollowButton = (ToggleButton) findViewById(R.id.activity_profile_follow_button);

        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(Boolean.TRUE);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        }

        /*Drawable coverPic = getResources().getDrawable(R.drawable.concert);
        coverPic.setAlpha(150);
        mCoverPic.setImageDrawable(coverPic);*/

        mRecyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(), 2));

        ArrayList<ProfileAlbums> profileAlbum = new ArrayList<>();

        profileAlbum.add(new ProfileAlbums("Coldplay", 4, getResources().getDrawable(R.drawable.mountain_pic2)));
        profileAlbum.add(new ProfileAlbums("Florida Whistle", 1, getResources().getDrawable(R.drawable.mountain_pic2)));
        profileAlbum.add(new ProfileAlbums("One Direction", 2, getResources().getDrawable(R.drawable.mountain_pic2)));
        profileAlbum.add(new ProfileAlbums("Gangnam Style", 5, getResources().getDrawable(R.drawable.mountain_pic2)));
        profileAlbum.add(new ProfileAlbums("Arijit Singh", 25, getResources().getDrawable(R.drawable.mountain_pic2)));


        mRecyclerView.setAdapter(new RecyclerViewAdapter(getBaseContext(), profileAlbum));

        mCollapsingToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, ProfileDescriptionActivity.class));
            }
        });


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
                startActivity(new Intent(ProfileActivity.this, SignInActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

        Context mContext;
        ArrayList<ProfileAlbums> mProfileAlbum;

        public RecyclerViewAdapter(Context context, ArrayList<ProfileAlbums> profileAlbum) {
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
            ProfileAlbums temp = mProfileAlbum.get(position);
            holder.mAlbumName.setText(temp.getAlbumName());
            holder.mNumOfSongs.setText(temp.getNumOfSongs() + " Songs");
            holder.mImageView.setImageDrawable(temp.getDrawable());
        }

        @Override
        public int getItemCount() {
            return this.mProfileAlbum.size();
        }
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mImageView;
        TextView mAlbumName;
        TextView mNumOfSongs;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mImageView = (ImageView) itemView.findViewById(R.id.activity_profile_card_item_album_pic);
            mAlbumName = (TextView) itemView.findViewById(R.id.activity_profile_card_item_album_name);
            mNumOfSongs = (TextView) itemView.findViewById(R.id.activity_profile_card_item_song_number);
        }

        @Override
        public void onClick(View view) {
            startActivity(new Intent(ProfileActivity.this,PlaylistActivity.class));
        }
    }
}
