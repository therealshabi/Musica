package com.finiteloop.musica;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.finiteloop.musica.SharedPreferencesUtils.UserDataSharedPreference;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatRoom extends AppCompatActivity {

    Toolbar mToolbar;
    String mRoomName;
    String mUserName;
    DatabaseReference mRoot;
    EditText mMessage;
    ImageView mSend;
    LinearLayout mParent;
    NestedScrollView scroller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        mParent = (LinearLayout) findViewById(R.id.parent);
        mMessage = (EditText) findViewById(R.id.ChatActivityMessage);
        mSend = (ImageView) findViewById(R.id.send);
        scroller = (NestedScrollView) findViewById(R.id.scroller);
        mToolbar = (Toolbar) findViewById(R.id.ChatRoomActivityToolbar);
        setSupportActionBar(mToolbar);

        mRoomName = getIntent().getStringExtra("Room Name");
        mUserName = UserDataSharedPreference.getUsername(getBaseContext());

        scroller.postDelayed(new Runnable() {
            @Override
            public void run() {
                scroller.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 100);

        mRoot = FirebaseDatabase.getInstance().getReference().child("Rooms").child(mRoomName).child("Messages");

        Log.d("UserName", mUserName);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(mRoomName);
        }

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp_key = mRoot.push().getKey();

                DatabaseReference message_root = mRoot.child(temp_key);
                Map<String, Object> message = new HashMap<>();
                message.put("Name", mUserName);
                message.put("Message", mMessage.getText().toString());

                message_root.updateChildren(message);
                mMessage.setText("");
                scroller.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scroller.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                }, 100);
            }
        });

        mRoot.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                append_conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                append_conversation(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void append_conversation(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();
        TextView myName, myMessage, othersMessage, othersName;
        while (i.hasNext()) {
            String message = ((DataSnapshot) i.next()).getValue().toString();
            String user = ((DataSnapshot) i.next()).getValue().toString();

            if (user.equals(mUserName)) {
                View v = LayoutInflater.from(getApplicationContext()).inflate(getResources().getLayout(R.layout.my_message), null);
                myName = (TextView) v.findViewById(R.id.myName);
                myMessage = (TextView) v.findViewById(R.id.myMessage);
                myMessage.setText(message);
                myName.setText(user);
                mParent.addView(v);
                scroller.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scroller.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                }, 100);
            } else {
                View v = LayoutInflater.from(getApplicationContext()).inflate(getResources().getLayout(R.layout.others_message), null);
                othersMessage = (TextView) v.findViewById(R.id.othersMessage);
                othersName = (TextView) v.findViewById(R.id.othersName);
                othersMessage.setText(message);
                othersName.setText(user);
                mParent.addView(v);
                scroller.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scroller.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                }, 100);
            }
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                supportFinishAfterTransition();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        scroller.postDelayed(new Runnable() {
            @Override
            public void run() {
                scroller.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 100);
    }

    @Override
    protected void onResume() {
        super.onResume();
        scroller.postDelayed(new Runnable() {
            @Override
            public void run() {
                scroller.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 100);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        scroller.postDelayed(new Runnable() {
            @Override
            public void run() {
                scroller.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 100);
    }
}
