package com.finiteloop.musica;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class AddPostActivity extends AppCompatActivity {

    Toolbar mToolbar;
    Spinner mGenreSpinner;
    ArrayList<String> mGenreArrayList = new ArrayList<>();
    ArrayAdapter<String> mGenreArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        mToolbar = (Toolbar) findViewById(R.id.activity_add_post_toolbar);
        mGenreSpinner = (Spinner) findViewById(R.id.activity_add_post_genre_spinner);
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(Boolean.TRUE);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        }

        mGenreArrayList.add("Select Genre of the Song");
        mGenreArrayList.add("Pop");
        mGenreArrayList.add("Hip-hop");
        mGenreArrayList.add("Rock");
        mGenreArrayList.add("Romatic");
        mGenreArrayList.add("Classic");
        mGenreArrayList.add("Bollywood");
        mGenreArrayList.add("Electronic");
        mGenreArrayList.add("Acoustic");
        mGenreArrayList.add("Opera");

        mGenreArrayAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, mGenreArrayList);
        mGenreSpinner.setAdapter(mGenreArrayAdapter);

        mGenreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
