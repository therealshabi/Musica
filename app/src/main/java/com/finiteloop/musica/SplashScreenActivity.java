package com.finiteloop.musica;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                if (mAuth.getCurrentUser() == null)
                    startActivity(new Intent(SplashScreenActivity.this, SignInActivity.class));
                else
                    startActivity(new Intent(SplashScreenActivity.this, HomeStreamActivity.class));
            }
        }, SPLASH_TIME_OUT);

    }
}
