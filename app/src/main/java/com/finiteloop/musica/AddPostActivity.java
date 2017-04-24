package com.finiteloop.musica;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.finiteloop.musica.NetworkUtils.MusicaServerAPICalls;
import com.finiteloop.musica.SharedPreferencesUtils.UserDataSharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddPostActivity extends AppCompatActivity {

    Toolbar mToolbar;
    Spinner mGenreSpinner;
    Button submitButton;
    EditText post_title;
    EditText post_description;
    EditText post_song_url;
    EditText post_image_url;
    String item; //for spinner
    JSONObject jsonObject;

    ProgressDialog progressDialog;

    ArrayList<String> mGenreArrayList = new ArrayList<>();
    ArrayAdapter<String> mGenreArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        mToolbar = (Toolbar) findViewById(R.id.activity_add_post_toolbar);
        mGenreSpinner = (Spinner) findViewById(R.id.activity_add_post_genre_spinner);
        post_title=(EditText)findViewById(R.id.activity_add_post_title_editText);
        post_description=(EditText)findViewById(R.id.activity_add_post_description_editText);
        post_song_url=(EditText)findViewById(R.id.activity_add_post_attachment_text);
        post_image_url=(EditText)findViewById(R.id.activity_add_post_add_attachment_pic_text);

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Posting...");

        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(Boolean.TRUE);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        }

        submitButton=(Button)findViewById(R.id.activity_add_post_submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), UserDataSharedPreference.getEmail(getBaseContext()) + "", Toast.LENGTH_SHORT).show();
                progressDialog.show();
                jsonObject = new JSONObject();
                try {
                    jsonObject.put("email_address", UserDataSharedPreference.getEmail(getBaseContext()));
                    jsonObject.put("post_title",post_title.getText().toString());
                    jsonObject.put("post_info",post_description.getText().toString());
                    jsonObject.put("post_genre_tag",item);
                    jsonObject.put("post_album_pic",post_image_url.getText().toString());
                    jsonObject.put("post_song_url",post_song_url.getText().toString());
                    jsonObject.put("user_profile_pic", UserDataSharedPreference.getProfileURL(getBaseContext()));
                    jsonObject.put("username", UserDataSharedPreference.getUsername(getBaseContext()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //REQUESTING FOR STORING USER_POST DETAIL IN MONGODB
                new MusicaServerAPICalls() {
                    @Override
                    public void isRequestSuccessful(boolean isSuccessful, String message) {
                        if (isSuccessful) {
                            Toast.makeText(getBaseContext(), "Posted Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                            progressDialog.dismiss();
                            startActivity(new Intent(AddPostActivity.this, HomeStreamActivity.class));
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getBaseContext(), "There was an Error while posting", Toast.LENGTH_SHORT).show();
                        }
                    }
                }.submitPostByUser(getBaseContext(), jsonObject);
            }
        });

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
                item = parent.getItemAtPosition(position).toString();
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
