package com.finiteloop.musica;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class HomeStreamActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    ImageView mNavigationToggle;
    private ImageView searchButton;
    private ImageView profileButton;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_stream);

        searchButton = (ImageView) findViewById(R.id.activity_home_stream_searchButton);
        profileButton = (ImageView) findViewById(R.id.activity_home_stream_profileButton);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_home_stream_drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.activity_home_stream_navigationView);

        mNavigationToggle = (ImageView) findViewById(R.id.home_stream_activity_toolbar_navigation_toggle);

        recyclerView = (RecyclerView) findViewById(R.id.home_stream_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recyclerView.setAdapter(new RecyclerViewAdapter());

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "Search Activity shows up ", Toast.LENGTH_SHORT).show();
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "Profile Activity shows up ", Toast.LENGTH_SHORT).show();
            }
        });

        mNavigationToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
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
                        Toast.makeText(getBaseContext(), "Explore Button Pressed", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    case R.id.navigation_menu_playlist: {
                        Toast.makeText(getBaseContext(), "Playlist Button Pressed", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    case R.id.navigation_menu_notifications: {
                        Toast.makeText(getBaseContext(), "Notifications Button Pressed", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    case R.id.navigation_menu_profile: {
                        Toast.makeText(getBaseContext(), "Profile Button Pressed", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getBaseContext(), "SignOut Button Pressed", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
                return false;
            }
        });
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
            return 5;
        }
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public RecyclerViewHolder(View itemView) {
            super(itemView);
        }
    }
}
