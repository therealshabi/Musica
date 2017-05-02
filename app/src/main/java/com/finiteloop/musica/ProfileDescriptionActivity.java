package com.finiteloop.musica;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.finiteloop.musica.Models.UserModel;
import com.finiteloop.musica.NetworkUtils.MusicaServerAPICalls;
import com.finiteloop.musica.SharedPreferencesUtils.UserDataSharedPreference;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfileDescriptionActivity extends AppCompatActivity {

    Button mEditProfile;
    String mUsername;
    String mProfilePic;
    String mEmailId;
    CircularImageView mProfilePicImageView;
    TextView mUsernameTextView;
    Button mEditDescriptionDialogButton;
    EditText mEditDescriptionDialogEditText;
    TextView mDescriptionTextView;
    Context mContext;
    TextView mNumOfFollowers;
    TextView mNumOfPosts;
    TextView mNumOfFollowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_description);

        mUsername = getIntent().getStringExtra("Username");
        mProfilePic = getIntent().getStringExtra("Profile Pic");
        mEmailId = getIntent().getStringExtra("Email Id");
        mContext = this;

        mUsernameTextView = (TextView) findViewById(R.id.activity_profile_description_username);
        mProfilePicImageView = (CircularImageView) findViewById(R.id.activity_profile_description_profile_pic);
        mEditProfile = (Button) findViewById(R.id.activity_profile_description_edit_profile_button);
        mDescriptionTextView = (TextView) findViewById(R.id.activity_profile_description_description);
        mNumOfFollowers = (TextView) findViewById(R.id.activity_profile_description_num_of_followers);
        mNumOfFollowing = (TextView) findViewById(R.id.activity_profile_description_num_of_following);
        mNumOfPosts = (TextView) findViewById(R.id.activity_profile_description_num_of_posts);

        if (UserDataSharedPreference.getEmail(getBaseContext()).equals(mEmailId) && UserDataSharedPreference.getDescription(getBaseContext()) != null && !UserDataSharedPreference.getDescription(getBaseContext()).equals("")) {
            mDescriptionTextView.setText(UserDataSharedPreference.getDescription(getBaseContext()));
        }

        mEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(ProfileDescriptionActivity.this,MainActivity.class));
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                View v = LayoutInflater.from(mContext).inflate(R.layout.edit_description, null);
                builder.setView(v);
                final AlertDialog dialog = builder.create();
                dialog.show();

                mEditDescriptionDialogButton = (Button) v.findViewById(R.id.edit_description_profile_button);
                mEditDescriptionDialogEditText = (EditText) v.findViewById(R.id.edit_description_edit_text);

                mEditDescriptionDialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!mEditDescriptionDialogEditText.getText().toString().equals("")) {
                            JSONObject object = parseDescriptionToJSON(mEditDescriptionDialogEditText.getText().toString());
                            new MusicaServerAPICalls() {
                                @Override
                                public void isRequestSuccessful(boolean isSuccessful, String message) {
                                    if (isSuccessful) {
                                        Toast.makeText(getBaseContext(), "Description Updated Successfully", Toast.LENGTH_SHORT).show();
                                        UserDataSharedPreference.setDescription(getBaseContext(), mEditDescriptionDialogEditText.getText().toString());
                                        mDescriptionTextView.setText(UserDataSharedPreference.getDescription(getBaseContext()));
                                    }
                                }
                            }.postUserProfileDescription(getBaseContext(), object);
                        }
                        dialog.dismiss();
                    }
                });

            }
        });
    }

    private JSONObject parseDescriptionToJSON(String text) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email_address", UserDataSharedPreference.getEmail(getBaseContext()));
            jsonObject.put("description", text);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("JSON Error", e.toString());
        }
        return jsonObject;
    }

    //Parsing JSON for User user search based on email
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
            JSONArray p = d.getJSONArray("posts");
            ArrayList<String> post = new ArrayList<>();
            for (int j = 0; j < p.length(); j++) {
                post.add(p.getString(j));
            }
            u.setPosts(post);            //Log.d("User", u.toString());
        } catch (JSONException e) {
            Log.d("Exception", e.getMessage());
        }
        //Log.d("User",user.get(0).toString());
        return u;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mEmailId.equals(UserDataSharedPreference.getEmail(getBaseContext()))) {
            mEditProfile.setVisibility(View.VISIBLE);
        } else {
            mEditProfile.setVisibility(View.GONE);
        }

        new MusicaServerAPICalls() {
            @Override
            public void isRequestSuccessful(boolean isSuccessful, String message) {
                if (isSuccessful) {
                    UserModel user = parseUserData(message);
                    Log.d("Posts", user.getPosts().toString());
                    mNumOfPosts.setText("" + user.getPosts().size());
                    mNumOfFollowers.setText("" + user.getFollowerList().size());
                    mNumOfFollowing.setText("" + user.getFollowingList().size());
                }
            }
        }.getUserOnEmail(getBaseContext(), mEmailId);


        new MusicaServerAPICalls() {
            @Override
            public void isRequestSuccessful(boolean isSuccessful, String message) {
                if (isSuccessful) {
                    if (!message.equals(""))
                        mDescriptionTextView.setText(message);
                    else {
                        mDescriptionTextView.setText("No Description Provided");
                    }
                }
            }
        }.getUserDescription(getBaseContext(), mEmailId);
        mUsernameTextView.setText(mUsername);
        Picasso.with(getBaseContext()).load(Uri.parse(mProfilePic)).into(mProfilePicImageView);
    }
}
