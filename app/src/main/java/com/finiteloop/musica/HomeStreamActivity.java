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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.finiteloop.musica.SharedPreferencesUtils.UserDataSharedPreference;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class HomeStreamActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_stream);

        mContext = this;
        mAuth = FirebaseAuth.getInstance();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_home_stream_drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.activity_home_stream_navigationView);
        mAddPostCardView = (CardView) findViewById(R.id.activity_home_stream_add_post_card_view);
        mHelloUserText = (TextView) findViewById(R.id.activity_home_stream_hello_user_text);
        mHomeStreamPostProfileImageView = (CircularImageView) findViewById(R.id.activity_home_stream_add_post_profile_pic);

        mHelloUserText.setText("Hello " + UserDataSharedPreference.getUsername(getBaseContext()).toUpperCase() + "..Share something with the world");

        mToolbar = (Toolbar) findViewById(R.id.home_stream_activity_toolbar);

        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_dehaze_white_24dp));
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = (RecyclerView) findViewById(R.id.home_stream_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recyclerView.setAdapter(new RecyclerViewAdapter(getBaseContext()));

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
        Log.d("Path", UserDataSharedPreference.getProfileURL(getBaseContext()).toString());

        Uri uri = Uri.parse(UserDataSharedPreference.getProfileURL(getBaseContext()));
        Picasso.with(getApplicationContext()).load(uri).into(mHomeStreamPostProfileImageView);
        Picasso.with(getApplicationContext()).load(uri).into(mNavigationViewHeaderProfilePic);

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

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

        Context mContext;

        public RecyclerViewAdapter(Context context) {
            mContext = context;
        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.homestream_card_view, parent, false);
            return new RecyclerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        }

        @Override
        public int getItemCount() {
            return 8;
        }
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView mUserName;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            mUserName = (TextView) itemView.findViewById(R.id.activity_home_stream_post_user_name_text);
        }
    }

}
