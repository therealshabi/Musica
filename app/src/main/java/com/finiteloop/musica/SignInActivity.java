package com.finiteloop.musica;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity {

    CoordinatorLayout mCoordinatorLayout;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_sign_in);
        Drawable background = getResources().getDrawable(R.drawable.sign_in_background);
        background.setAlpha(150);
        mCoordinatorLayout.setBackground(background);
    }
}
