package com.finiteloop.musica;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.finiteloop.musica.SharedPreferencesUtils.UserDataSharedPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {

    LinearLayout mSignUp;
    Button mSignIn;
    ImageView mTwitterSignIn, mFacebookSignIn, mGoogleSignIn;
    FirebaseAuth mAuth;
    EditText mUsername, mPassword;
    ProgressDialog mProgressDialog;
    DatabaseReference mDatabase;
    String username, password, email;
    CoordinatorLayout mCoordinatorLayout;
    String mailId;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();

        mSignUp = (LinearLayout) findViewById(R.id.activity_sign_in_sign_up_text);
        mSignIn = (Button) findViewById(R.id.activity_sign_in_button);
        mFacebookSignIn = (ImageView) findViewById(R.id.activity_sign_in_fb);
        mTwitterSignIn = (ImageView) findViewById(R.id.activity_sign_in_twitter);
        mGoogleSignIn = (ImageView) findViewById(R.id.activity_sign_in_google_plus);
        mUsername = (EditText) findViewById(R.id.activity_sign_in_username);
        mPassword = (EditText) findViewById(R.id.activity_sign_in_password);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_sign_in);

        mProgressDialog = new ProgressDialog(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });

        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateCredentials()) {
                    mProgressDialog.setMessage("Signing in, Please Wait...");
                    mProgressDialog.show();
                    mDatabase.child("Users").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null) {
                                Log.d("Check", dataSnapshot.toString());
                                email = dataSnapshot.getValue(String.class);
                                if (email != null) {
                                    mailId = email;
                                    login(email, password);
                                } else {
                                    mProgressDialog.dismiss();
                                    mUsername.setError("Username " + username + " does not exist");
                                    Snackbar.make(mCoordinatorLayout, "Username " + username + " does not exist", Snackbar.LENGTH_SHORT).show();
                                }
                            } else {
                                mProgressDialog.dismiss();
                                Snackbar.make(mCoordinatorLayout, "Username does not exist", Snackbar.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            mProgressDialog.dismiss();
                        }
                    });
                }
            }
        });
    }

    private void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getBaseContext(), "Signed In Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                    mProgressDialog.dismiss();
                    UserDataSharedPreference.setUsername(getBaseContext(), username);
                    UserDataSharedPreference.setEmail(getBaseContext(), mailId);
                    startActivity(new Intent(SignInActivity.this, HomeStreamActivity.class));
                } else {
                    Log.d("Error", task.getException().getMessage());
                    mProgressDialog.dismiss();
                    mPassword.setError(task.getException().getMessage());
                    Snackbar.make(mCoordinatorLayout, task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean validateCredentials() {
        username = mUsername.getText().toString().trim();
        password = mPassword.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Snackbar.make(mCoordinatorLayout, "Username cannot be empty", Snackbar.LENGTH_SHORT).show();
            mUsername.setError("Username cannot be empty");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            Snackbar.make(mCoordinatorLayout, "Password cannot be empty", Snackbar.LENGTH_SHORT).show();
            mPassword.setError("Password cannot be empty");
            return false;
        }
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mAuth.getCurrentUser() != null) {
            finish();
        }
    }
}
