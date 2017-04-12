package com.finiteloop.musica;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class HomeStreamActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    Toolbar mToolbar;
    FirebaseAuth mAuth;
    CardView mAddPostCardView;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_stream);

        mAuth = FirebaseAuth.getInstance();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_home_stream_drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.activity_home_stream_navigationView);
        mAddPostCardView = (CardView) findViewById(R.id.activity_home_stream_add_post_card_view);

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
        recyclerView.setAdapter(new RecyclerViewAdapter());

        mAddPostCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeStreamActivity.this, AddPostActivity.class));
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_menu_home: {
                        Toast.makeText(getBaseContext(), "Home Button Pressed", Toast.LENGTH_SHORT).show();
                        return true;
                    }
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
                        startActivity(new Intent(HomeStreamActivity.this, ProfileActivity.class));
                        return true;
                    }
                    case R.id.navigation_menu_report_and_feedback: {
                        Toast.makeText(getBaseContext(), "Report And Feedback Button Pressed", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    case R.id.navigation_menu_settings: {
                        Toast.makeText(getBaseContext(), "Settings Button Pressed", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    case R.id.navigation_menu_signOut: {
                        mAuth.signOut();
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        finish();
                        startActivity(new Intent(HomeStreamActivity.this, SignInActivity.class));
                        Toast.makeText(getBaseContext(), "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                        return true;
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

        public RecyclerViewHolder(View itemView) {
            super(itemView);
        }
    }

}
