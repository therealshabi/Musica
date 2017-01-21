package com.finiteloop.musica;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProfileDescriptionActivity extends AppCompatActivity {

    Button mFollowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_description);

        mFollowButton = (Button) findViewById(R.id.activity_profile_description_follow_button);

        mFollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileDescriptionActivity.this,MainActivity.class));
            }
        });
    }
}
