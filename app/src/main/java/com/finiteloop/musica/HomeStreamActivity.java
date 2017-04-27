package com.finiteloop.musica;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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
import com.finiteloop.musica.SharedPreferencesUtils.UserDataSharedPreference;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeStreamActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public RecyclerView recyclerView;
    public ArrayList<PostModel> homeStreamPostArrayList;
    Toolbar mToolbar;
    FirebaseAuth mAuth;
    CardView mAddPostCardView;
    TextView mHelloUserText;
    TextView mNavigationViewHeaderTextUsername;
    TextView mNavigationViewHeaderTextEmail;
    CircularImageView mNavigationViewHeaderProfilePic;
    Context mContext;
    CircularImageView mHomeStreamPostProfileImageView;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_stream);

        mContext = this;
        mAuth = FirebaseAuth.getInstance();

        homeStreamPostArrayList=new ArrayList<>();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_home_stream_drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.activity_home_stream_navigationView);
        mAddPostCardView = (CardView) findViewById(R.id.activity_home_stream_add_post_card_view);
        mHelloUserText = (TextView) findViewById(R.id.activity_home_stream_hello_user_text);
        mHomeStreamPostProfileImageView = (CircularImageView) findViewById(R.id.activity_home_stream_add_post_profile_pic);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.activity_home_stream_swipe_refresh_layout);

        mHelloUserText.setText("Hello " + UserDataSharedPreference.getUsername(getBaseContext()).toUpperCase() + "..Share something with the world");

        mToolbar = (Toolbar) findViewById(R.id.home_stream_activity_toolbar);

        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_dehaze_white_24dp));
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                fetchData();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.home_stream_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));


        mAddPostCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeStreamActivity.this, AddPostActivity.class));
            }
        });

        mNavigationViewHeaderTextUsername = (TextView) navigationView.getHeaderView(0).findViewById(R.id.home_stream_navigation_profile_name);
        mNavigationViewHeaderTextEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.home_stream_navigation_profile_email);
        mNavigationViewHeaderProfilePic = (CircularImageView) navigationView.getHeaderView(0).findViewById(R.id.home_stream_navigation_profile_pic);

        mNavigationViewHeaderTextUsername.setText(UserDataSharedPreference.getUsername(getBaseContext()).toUpperCase());
        mNavigationViewHeaderTextEmail.setText(UserDataSharedPreference.getEmail(getBaseContext()));
        Picasso.with(getBaseContext()).load(Uri.parse(UserDataSharedPreference.getProfileURL(getBaseContext()))).into(mNavigationViewHeaderProfilePic);
      //  Log.d("Path", UserDataSharedPreference.getProfileURL(getBaseContext()).toString());

      //  Uri uri = Uri.parse(UserDataSharedPreference.getProfileURL(getBaseContext()));
      //  Picasso.with(getApplicationContext()).load(uri).into(mHomeStreamPostProfileImageView);
      //  Picasso.with(getApplicationContext()).load(uri).into(mNavigationViewHeaderProfilePic);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    /*case R.id.navigation_menu_home: {
                        Toast.makeText(getBaseContext(), "Home Button Pressed", Toast.LENGTH_SHORT).show();
                        return true;
                    }*/
                    case R.id.navigation_menu_explore: {
                        //Toast.makeText(getBaseContext(), "Explore Button Pressed", Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(HomeStreamActivity.this, SearchActivity.class));
                        return true;
                    }
                    case R.id.navigation_menu_playlist: {
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(HomeStreamActivity.this, PlaylistActivity.class));
                        return true;
                    }
                    case R.id.navigation_menu_notifications: {
                        Toast.makeText(getBaseContext(), "Notifications Button Pressed", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    case R.id.navigation_menu_profile: {
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        Intent i = new Intent(HomeStreamActivity.this, ProfileActivity.class);
                        i.putExtra("Username", UserDataSharedPreference.getUsername(getBaseContext()));
                        i.putExtra("Profile Pic", UserDataSharedPreference.getProfileURL(getBaseContext()));
                        startActivity(i);
                        return true;
                    }
                    case R.id.navigation_menu_report_and_feedback: {
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(getBaseContext(), "Report And Feedback Button Pressed", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    case R.id.navigation_menu_settings: {
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(getBaseContext(), "Settings Button Pressed", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    case R.id.navigation_menu_signOut: {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setMessage("Are you sure you want to Sign Out?").setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAuth.signOut();
                                UserDataSharedPreference.removeAllSharedPreferences(getBaseContext());
                                mDrawerLayout.closeDrawer(GravityCompat.START);
                                finish();
                                startActivity(new Intent(HomeStreamActivity.this, SignInActivity.class));
                                Toast.makeText(getBaseContext(), "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public void fetchData()
    {
        new MusicaServerAPICalls() {
            @Override
            public void isRequestSuccessful(boolean isSuccessful, String message) {
                if (isSuccessful) {
                    try {
                        homeStreamPostArrayList.clear();
                        homeStreamPostArrayList = parseJsonResponse(message);
                        recyclerView.setAdapter(new RecyclerViewAdapter(getBaseContext(),homeStreamPostArrayList));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getBaseContext(), "There was an error while loading...", Toast.LENGTH_SHORT).show();
                }
            }
        }.getHomeStreamPostOfUser(getBaseContext());
        swipeRefreshLayout.setRefreshing(false);
    }


    // json parsing of the getHomeStreamPostOfUser Api call
    public ArrayList<PostModel> parseJsonResponse(String response) throws JSONException {
        ArrayList<PostModel> arrayList = new ArrayList<>();

        JSONObject root = new JSONObject(response);
        JSONArray data = root.getJSONArray("data");
        for(int i=0;i<data.length();i++)
        {
            PostModel postModel=new PostModel();
            JSONObject post = data.getJSONObject(i);
            JSONArray no_of_likes = post.getJSONArray("user_like");
            JSONArray no_of_loves = post.getJSONArray("user_love");

            postModel.setPost_id(post.getString("_id"));
            postModel.setGenreTag(post.getString("post_genre_tag"));
            postModel.setTitle(post.getString("post_title"));
            postModel.setNo_of_likes(no_of_likes.length()+"");
            postModel.setNo_of_loves(no_of_loves.length()+"");
            postModel.setPost_pic_url(post.getString("post_album_pic"));
            postModel.setUser_profile_pic(post.getString("user_profile_pic"));
            postModel.setUsername(post.getString("username"));
            postModel.setPostURL(post.getString("post_song_url"));
            postModel.setTimeStamp(post.getString("post_time_stamp"));
            arrayList.add(postModel);
        }
        return arrayList;
    }

    @Override
    public void onRefresh() {
        fetchData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Picasso.with(getBaseContext()).load(Uri.parse(UserDataSharedPreference.getProfileURL(getBaseContext()))).into(mHomeStreamPostProfileImageView);
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

        Context mContext;
        private ArrayList<PostModel> homeStreamPostOfUser;

        public RecyclerViewAdapter(Context context, ArrayList<PostModel> homeStreamPostOfUser) {
            mContext = context;
            this.homeStreamPostOfUser=homeStreamPostOfUser;
        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.homestream_card_view, parent, false);
            return new RecyclerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
         if(homeStreamPostOfUser!=null){
             final PostModel postModel = homeStreamPostOfUser.get(position);
             holder.mTitle.setText(postModel.getTitle());
             holder.mGenreTag.setText(postModel.getGenreTag());
             holder.no_of_likes.setText(postModel.getNo_of_likes() + " Likes");
             holder.no_of_loves.setText(postModel.getNo_of_loves() + " Loves");
             if(postModel.getPost_pic_url().isEmpty())
             {
                 holder.post_imageView.setImageResource(R.drawable.mountain_pic2);
             }
             else{
                 Picasso.with(mContext).load(Uri.parse(postModel.getPost_pic_url())).into(holder.post_imageView);
             }

                if (postModel.getUser_profile_pic().isEmpty()) {
                    holder.post_imageView.setImageResource(R.drawable.ic_social);
                } else {
                    Picasso.with(getBaseContext()).load(Uri.parse(postModel.getUser_profile_pic())).into(holder.profileImage);
                }

                holder.mUserName.setText(postModel.getUsername());



             new MusicaServerAPICalls() {
                 @Override
                 public void isRequestSuccessful(boolean isSuccessful, String message) {
                     if (isSuccessful) {
                             ArrayList<String> emailAddress = new ArrayList<>();
                         try {
                             emailAddress = parseJsonEmailAddressResponse(message);
                         } catch (JSONException e) {
                             e.printStackTrace();
                         }
                         if(emailAddress.contains(UserDataSharedPreference.getEmail(getBaseContext()))){
                             holder.likeButton.setLiked(true);
                         } else{
                            holder.likeButton.setLiked(false);
                         }

                     } else {
                         Toast.makeText(getBaseContext(), "There was an error while updating like status...", Toast.LENGTH_SHORT).show();
                     }
                 }
             }.getEmailOfUserWhoLikedPost(getBaseContext(),postModel.getPost_id());



             new MusicaServerAPICalls() {
                 @Override
                 public void isRequestSuccessful(boolean isSuccessful, String message) {
                     if (isSuccessful) {
                         ArrayList<String> emailAddress = new ArrayList<>();
                         try {
                             emailAddress = parseJsonEmailAddressResponse(message);
                         } catch (JSONException e) {
                             e.printStackTrace();
                         }
                         if(emailAddress.contains(UserDataSharedPreference.getEmail(getBaseContext()))){
                             holder.loveButton.setLiked(true);
                         } else{
                             holder.loveButton.setLiked(false);
                         }

                     } else {
                         Toast.makeText(getBaseContext(), "There was an error while updating love status...", Toast.LENGTH_SHORT).show();
                     }
                 }
             }.getEmailOfUserWhoLovedPost(getBaseContext(),postModel.getPost_id());


             holder.likeButton.setOnLikeListener(new OnLikeListener() {


                 @Override
                 public void liked(LikeButton likeButton) {
                     JSONObject jsonObject = new JSONObject();
                     try {
                         jsonObject.put("user_liked_email_address",UserDataSharedPreference.getEmail(getBaseContext()));
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }
                     new MusicaServerAPICalls() {
                         @Override
                         public void isRequestSuccessful(boolean isSuccessful, String message) {
                             if (isSuccessful) {

                             } else {
                                 Toast.makeText(getBaseContext(), "There was an error while liking the post...", Toast.LENGTH_SHORT).show();
                             }
                         }
                     }.submitLikeByUser(getBaseContext(),jsonObject,postModel.getPost_id());
                 }


                 @Override
                 public void unLiked(LikeButton likeButton) {
                     JSONObject jsonObject = new JSONObject();
                     try {
                         jsonObject.put("user_liked_email_address",UserDataSharedPreference.getEmail(getBaseContext()));
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }
                     new MusicaServerAPICalls() {
                         @Override
                         public void isRequestSuccessful(boolean isSuccessful, String message) {
                             if (isSuccessful) {

                             } else {
                                 Toast.makeText(getBaseContext(), "There was an error while unliking the post...", Toast.LENGTH_SHORT).show();
                             }
                         }
                     }.submitUnLikeByUser(getBaseContext(),jsonObject,postModel.getPost_id());
                 }
             });

             holder.loveButton.setOnLikeListener(new OnLikeListener() {

                 @Override
                 public void liked(LikeButton likeButton) {
                     JSONObject jsonObject = new JSONObject();
                     try {
                         jsonObject.put("user_loved_email_address",UserDataSharedPreference.getEmail(getBaseContext()));
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }
                     new MusicaServerAPICalls() {
                         @Override
                         public void isRequestSuccessful(boolean isSuccessful, String message) {
                             if (isSuccessful) {

                             } else {
                                 Toast.makeText(getBaseContext(), "There was an error while loving the post...", Toast.LENGTH_SHORT).show();
                             }
                         }
                     }.submitLoveByUser(getBaseContext(),jsonObject,postModel.getPost_id());
                 }


                 @Override
                 public void unLiked(LikeButton likeButton) {
                     JSONObject jsonObject = new JSONObject();
                     try {
                         jsonObject.put("user_loved_email_address",UserDataSharedPreference.getEmail(getBaseContext()));
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }
                     new MusicaServerAPICalls() {
                         @Override
                         public void isRequestSuccessful(boolean isSuccessful, String message) {
                             if (isSuccessful) {

                             } else {
                                 Toast.makeText(getBaseContext(), "There was an error while unloving the post...", Toast.LENGTH_SHORT).show();
                             }
                         }
                     }.submitUnLoveByUser(getBaseContext(),jsonObject,postModel.getPost_id());
                 }
             });

             holder.no_of_likes.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     Intent intent = new Intent(getBaseContext(),LikeActivity.class);
                     intent.putExtra("POST_ID",postModel.getPost_id());
                     startActivity(intent);
                 }
             });

             holder.no_of_loves.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     Intent intent = new Intent(getBaseContext(),LoveActivity.class);
                     intent.putExtra("POST_ID",postModel.getPost_id());
                     startActivity(intent);
                 }
             });

         }
        }

        private ArrayList<String> parseJsonEmailAddressResponse(String message) throws JSONException {
            ArrayList<String> temp = new ArrayList<>();
            JSONObject root = new JSONObject(message);
            JSONArray data = root.getJSONArray("data");
            for(int i=0;i<data.length();i++){
                String value = data.getString(i);
                temp.add(value);
            }
            return temp;
        }

        @Override
        public int getItemCount() {
           return homeStreamPostOfUser.size();
        }
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mUserName;
        TextView mGenreTag;
        TextView mTitle;
        TextView no_of_likes;
        TextView no_of_loves;
        ImageView post_imageView;
        CircularImageView profileImage;
        LikeButton likeButton;
        LikeButton loveButton;
        String mMusicURL, mCoverURL, mMusicTitle;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mUserName = (TextView) itemView.findViewById(R.id.activity_home_stream_post_user_name_text);
            mGenreTag = (TextView) itemView.findViewById(R.id.activity_home_stream_genre_tag);
            mTitle = (TextView) itemView.findViewById(R.id.activity_home_stream_title);
            no_of_likes = (TextView) itemView.findViewById(R.id.activity_home_stream_no_of_likes);
            no_of_loves = (TextView) itemView.findViewById(R.id.activity_home_stream_no_of_loves);
            post_imageView = (ImageView) itemView.findViewById(R.id.activity_home_stream_cardView1_picture);
            profileImage = (CircularImageView) itemView.findViewById(R.id.activity_home_stream_card_profile);
            likeButton = (LikeButton) itemView.findViewById(R.id.thumb);
            loveButton = (LikeButton) itemView.findViewById(R.id.like);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(mContext, MusicPlayer.class);
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
