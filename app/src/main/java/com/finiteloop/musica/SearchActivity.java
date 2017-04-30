package com.finiteloop.musica;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.finiteloop.musica.Models.UserModel;
import com.finiteloop.musica.NetworkUtils.MusicaServerAPICalls;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import rx.Observable;
import rx.Observer;

public class SearchActivity extends AppCompatActivity {

    EditText mQueryEditText;
    TextView mNoUserFoundText;
    Context activityContext;
    private Toolbar toolbar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar=(Toolbar)findViewById(R.id.searchActivityToolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }

        mQueryEditText = (EditText) findViewById(R.id.activity_search_query_edit_text);
        recyclerView = (RecyclerView) findViewById(R.id.activity_search_recycler_view);
        mNoUserFoundText = (TextView) findViewById(R.id.no_user_found_text);

        ArrayList<UserModel> arr = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerViewAdapter(arr));

        activityContext = this;


        //RxJava
        Observable<CharSequence> mObservable = RxTextView.textChanges(mQueryEditText);
        mObservable.subscribe(new Observer<CharSequence>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getBaseContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(CharSequence charSequence) {
                new MusicaServerAPICalls() {
                    @Override
                    public void isRequestSuccessful(boolean isSuccessful, String message) {
                        if (isSuccessful) {
                            mNoUserFoundText.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            ArrayList<UserModel> model = parseData(message);
                            recyclerView.setAdapter(new RecyclerViewAdapter(model));

                        } else {
                            mNoUserFoundText.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                    }
                }.searchUserRequest(getBaseContext(), charSequence.toString());
            }
        });

    }

    //Parsing JSON for User Search in Search Activity
    private ArrayList<UserModel> parseData(String message) {
        ArrayList<UserModel> user = new ArrayList<>();
        try {
            JSONArray arr = new JSONArray(message);
            for (int i = 0; i < arr.length(); i++) {
                UserModel u = new UserModel();
                JSONObject d = arr.getJSONObject(i);
                u.setUsername(d.getString("user_name"));
                u.setProfilePicUrl(d.getString("profile_pic"));
                u.setEmail(d.getString("email_address"));
                Log.d("User", u.toString());
                user.add(u);
            }
        } catch (JSONException e) {
            Log.d("Exception", e.getMessage());
        }
        //Log.d("User",user.get(0).toString());
        return user;
    }

    //Parsing JSON for User user serach based on email
    private UserModel parseUserData(String message) {
        UserModel u = new UserModel();
        try {
            JSONObject d = new JSONObject(message);
            u.setUsername(d.getString("user_name"));
            u.setProfilePicUrl(d.getString("profile_pic"));
            u.setEmail(d.getString("email_address"));
            JSONArray st = d.getJSONArray("following");
            ArrayList<String> following = new ArrayList<>();
            for (int j = 0; j < st.length(); j++) {
                following.add(st.getString(j));
            }
            u.setFollowingList(following);
            JSONArray f = d.getJSONArray("followers");
            ArrayList<String> followers = new ArrayList<>();
            for (int j = 0; j < f.length(); j++) {
                followers.add(f.getString(j));
            }
            u.setFollowerList(followers);
            u.setId(d.getString("_id"));
            u.setUserInfo(d.getString("user_info"));
            //Log.d("User", u.toString());
        } catch (JSONException e) {
            Log.d("Exception", e.getMessage());
        }
        //Log.d("User",user.get(0).toString());
        return u;
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mEmailTextView;
        TextView mUsernameTextView;
        ImageView mProfileImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mUsernameTextView = (TextView) itemView.findViewById(R.id.activity_search_username_text);
            mEmailTextView = (TextView) itemView.findViewById(R.id.activity_search_email_text);
            mProfileImageView = (ImageView) itemView.findViewById(R.id.activity_search_profile_image);
        }

        @Override
        public void onClick(View v) {
            String email = mEmailTextView.getText().toString();
            Log.d("Email", email);
            new MusicaServerAPICalls() {
                @Override
                public void isRequestSuccessful(boolean isSuccessful, String message) {
                    if (isSuccessful) {
                        UserModel user = parseUserData(message);
                        //Log.d("User",user.toString());
                        Intent i = new Intent(activityContext, ProfileActivity.class);
                        i.putExtra("Username", user.getUsername());
                        //Log.d("USer",user.getUsername());
                        i.putExtra("Profile Pic", user.getProfilePicUrl());
                        i.putExtra("Email Id", user.getEmail());
                        startActivity(i);
                    } else {
                        Toast.makeText(getBaseContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }.getUserOnEmail(getBaseContext(), email);
        }
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder>{
        ArrayList<UserModel> mUsers = new ArrayList<>();

        public RecyclerViewAdapter(ArrayList<UserModel> users) {
            if (users != null)
                mUsers.addAll(users);
            else {
                mUsers = new ArrayList<>();
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(getBaseContext()).inflate(R.layout.search_activity_card_view,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mEmailTextView.setText(mUsers.get(position).getEmail());
            holder.mUsernameTextView.setText(mUsers.get(position).getUsername());
            Picasso.with(getBaseContext()).load(Uri.parse(mUsers.get(position).getProfilePicUrl())).into(holder.mProfileImageView);
        }

        @Override
        public int getItemCount() {
            return mUsers.size();
        }
    }
}
