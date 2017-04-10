package com.finiteloop.musica;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    LinearLayout mSignUp;
    Button mSignIn;
    ImageView mTwitterSignIn, mFacebookSignIn, mGoogleSignIn;
    FirebaseAuth mAuth;
    EditText mUsername, mPassword;

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

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });

        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this,HomeStreamActivity.class));
            }
        });
    }
}
