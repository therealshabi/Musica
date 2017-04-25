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

import com.finiteloop.musica.NetworkUtils.MusicaServerAPICalls;
import com.finiteloop.musica.SharedPreferencesUtils.UserDataSharedPreference;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileDescriptionActivity extends AppCompatActivity {

    Button mEditProfile;
    String mUsername;
    String mProfilePic;
    CircularImageView mProfilePicImageView;
    TextView mUsernameTextView;
    Button mEditDescriptionDialogButton;
    EditText mEditDescriptionDialogEditText;
    TextView mDescriptionTextView;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_description);

        mUsername = getIntent().getStringExtra("Username");
        mProfilePic = getIntent().getStringExtra("Profile Pic");

        mContext = this;

        mUsernameTextView = (TextView) findViewById(R.id.activity_profile_description_username);
        mProfilePicImageView = (CircularImageView) findViewById(R.id.activity_profile_description_profile_pic);
        mEditProfile = (Button) findViewById(R.id.activity_profile_description_edit_profile_button);
        mDescriptionTextView = (TextView) findViewById(R.id.activity_profile_description_description);

        if (UserDataSharedPreference.getDescription(getBaseContext()) != null && !UserDataSharedPreference.getDescription(getBaseContext()).equals("")) {
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

    @Override
    protected void onStart() {
        super.onStart();
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
        }.getUserDescription(getBaseContext());
        mUsernameTextView.setText(mUsername);
        Picasso.with(getBaseContext()).load(Uri.parse(mProfilePic)).into(mProfilePicImageView);
    }
}
