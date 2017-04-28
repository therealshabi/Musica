package com.finiteloop.musica;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.finiteloop.musica.NetworkUtils.MusicaServerAPICalls;
import com.finiteloop.musica.SharedPreferencesUtils.UserDataSharedPreference;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class AddPostActivity extends AppCompatActivity {

    private static final int ADD_COVER_IMAGE_REQUEST_CODE = 1;
    private static final int ADD_POST_REQUEST_CODE = 2;
    private static final String TAG = "Permission";

    Toolbar mToolbar;
    Spinner mGenreSpinner;
    Button submitButton;
    EditText mPostTitle;
    EditText mPostDescription;
    TextView mPostSongURL;
    String item; //for spinner
    JSONObject jsonObject;
    TextView mUsernameTextView;
    CircularImageView mProfilePicImage;
    LinearLayout mAddAlbumCoverPic;
    ImageView mAlbumCoverPic;
    ImageButton mAddPost;
    StorageReference mStorageRef;
    String mCoverImageURL;
    String mPostURL;
    Uri mMusicPath;
    Uri mCoverPicPath;
    CheckBox mPrivateCheckBox;

    ProgressDialog progressDialog;

    ArrayList<String> mGenreArrayList = new ArrayList<>();
    ArrayAdapter<String> mGenreArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        mToolbar = (Toolbar) findViewById(R.id.activity_add_post_toolbar);
        mGenreSpinner = (Spinner) findViewById(R.id.activity_add_post_genre_spinner);
        mPostTitle = (EditText) findViewById(R.id.activity_add_post_title_editText);
        mPostDescription = (EditText) findViewById(R.id.activity_add_post_description_editText);
        mPostSongURL = (TextView) findViewById(R.id.activity_add_post_attachment_text);
        mUsernameTextView = (TextView) findViewById(R.id.activity_add_post_text);
        mProfilePicImage = (CircularImageView) findViewById(R.id.activity_add_post_profile_pic);
        mAddAlbumCoverPic = (LinearLayout) findViewById(R.id.activity_add_post_attachment_cover_pic_add);
        mAlbumCoverPic = (ImageView) findViewById(R.id.activity_add_post_add_attachment_pic);
        mAddPost = (ImageButton) findViewById(R.id.activity_add_post_attachment);
        submitButton = (Button) findViewById(R.id.activity_add_post_submit_button);
        mPrivateCheckBox = (CheckBox) findViewById(R.id.activity_add_post_private_checkbox);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait, Publishing your post...");

        mAddAlbumCoverPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, ADD_COVER_IMAGE_REQUEST_CODE);
            }
        });

        mAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStoragePermissionGranted()) {
                    Intent intent = new Intent();
                    intent.setType("audio/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, ADD_POST_REQUEST_CODE);
                }
            }
        });


        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(Boolean.TRUE);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getBaseContext(), UserDataSharedPreference.getEmail(getBaseContext()) + "", Toast.LENGTH_SHORT).show();
                progressDialog.show();

                if (mMusicPath != null && !mMusicPath.toString().isEmpty()) {
                    StorageReference mMusicRef = mStorageRef.child("Music").child(UserDataSharedPreference.getEmail(getBaseContext())).child(UUID.randomUUID().toString());
                    mMusicRef.putFile(mMusicPath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mPostURL = taskSnapshot.getDownloadUrl().toString();
                            addImage();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getBaseContext(), "There was some problem while adding the post", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                } else {
                    Toast.makeText(getBaseContext(), "Add a valid Music File", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
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

    private void addImage() {
        StorageReference mCover = mStorageRef.child("Album Cover").child(UserDataSharedPreference.getEmail(getBaseContext())).child(UUID.randomUUID().toString());
        if (mCoverPicPath != null && !mCoverPicPath.toString().isEmpty()) {
            mCover.putFile(mCoverPicPath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mCoverImageURL = taskSnapshot.getDownloadUrl().toString();
                    setJSON();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getBaseContext(), "Error occurred while adding album cover pic", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        } else {
            String uriPath = "android.resource://com.finiteloop.musica/drawable/placeholder";
            mCoverPicPath = Uri.parse(uriPath);
            mCover.putFile(mCoverPicPath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mCoverImageURL = taskSnapshot.getDownloadUrl().toString();
                    setJSON();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getBaseContext(), "Error occurred while adding album cover pic", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
    }

    public void setJSON() {
        jsonObject = new JSONObject();
        try {
            jsonObject.put("email_address", UserDataSharedPreference.getEmail(getBaseContext()));
            jsonObject.put("post_title", mPostTitle.getText().toString());
            jsonObject.put("post_info", mPostDescription.getText().toString());
            if (item.equals("Select Genre of the Song"))
                item = "";
            jsonObject.put("post_genre_tag", item);
            jsonObject.put("post_album_pic", mCoverImageURL);
            jsonObject.put("post_song_url", mPostURL);
            jsonObject.put("user_profile_pic", UserDataSharedPreference.getProfileURL(getBaseContext()));
            jsonObject.put("username", UserDataSharedPreference.getUsername(getBaseContext()));
            String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
            jsonObject.put("post_time_stamp", timeStamp);
            if (mPrivateCheckBox.isChecked()) {
                jsonObject.put("private_post", true);
            } else {
                jsonObject.put("private_post", false);
            }
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


    private boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {
                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_COVER_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            mCoverPicPath = data.getData();
            Picasso.with(getBaseContext()).load(mCoverPicPath).into(mAlbumCoverPic);
        }
        if (requestCode == ADD_POST_REQUEST_CODE && resultCode == RESULT_OK) {
            mMusicPath = data.getData();
            mPostSongURL.setText(mMusicPath.toString());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mUsernameTextView.setText(UserDataSharedPreference.getUsername(getBaseContext()));
        Picasso.with(getBaseContext()).load(Uri.parse(UserDataSharedPreference.getProfileURL(getBaseContext()))).into(mProfilePicImage);
    }
}
