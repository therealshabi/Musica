package com.finiteloop.musica;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.like.IconType;
import com.like.LikeButton;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private Button event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=(Button)findViewById(R.id.button_profileDescription);
        event=(Button)findViewById(R.id.button_event_notification_dialog_box);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getBaseContext(),ProfileDescriptionActivity.class);
                startActivity(intent);
            }
        });

        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                View view= getLayoutInflater().inflate(R.layout.event_invitation,null);
                builder.setView(view);
                final AlertDialog dialog=builder.create();
                dialog.show();
            }
        });
    }
}
