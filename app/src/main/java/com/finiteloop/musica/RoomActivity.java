package com.finiteloop.musica;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.finiteloop.musica.Models.Rooms;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RoomActivity extends AppCompatActivity {

    public static Context mContext;
    FloatingActionButton mFab;
    DatabaseReference mRoot;
    RecyclerView mRoomsList;
    Toolbar toolbar;
    SwipeRefreshLayout mRefresh;
    FirebaseRecyclerAdapter<Rooms, RoomHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        mFab = (FloatingActionButton) findViewById(R.id.AddRoomsFab);
        mRoomsList = (RecyclerView) findViewById(R.id.RoomsList);
        mRefresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        toolbar = (Toolbar) findViewById(R.id.RoomActivityToolbar);
        setSupportActionBar(toolbar);

        mContext = RoomActivity.this;

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        mRoomsList.setHasFixedSize(true);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mRoot = database.getReference().child("Rooms");


        mRoomsList.setLayoutManager(new LinearLayoutManager(this));

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Rooms, RoomHolder>(Rooms.class, R.layout.recycler_view_item, RoomHolder.class, mRoot) {
            @Override
            protected void populateViewHolder(RoomHolder viewHolder, Rooms model, int position) {
                viewHolder.mRoomName.setText(model.getRoomName());
                viewHolder.mRoomType.setText(model.getRoomType());
            }
        };

        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefresh.setRefreshing(true);
                firebaseRecyclerAdapter.notifyDataSetChanged();
                mRoomsList.setAdapter(firebaseRecyclerAdapter);
                mRefresh.setRefreshing(false);
            }
        });

        mRoomsList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();


        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(RoomActivity.this);
                View v = LayoutInflater.from(getBaseContext()).inflate(getResources().getLayout(R.layout.add_room_dialog), null);
                final EditText room = (EditText) v.findViewById(R.id.roomName);
                final EditText roomType = (EditText) v.findViewById(R.id.autoCompleteTextView);
                Button addRoom = (Button) v.findViewById(R.id.addRoom);
                Button cancel = (Button) v.findViewById(R.id.cancel);
                builder.setView(v);
                final AlertDialog dialog = builder.create();
                dialog.show();

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                addRoom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Rooms newRoom = new Rooms();
                        newRoom.setRoomName(room.getText().toString().trim());
                        newRoom.setRoomType(roomType.getText().toString().trim());
                        mRoot.child(room.getText().toString().trim()).setValue(newRoom);
                        Toast.makeText(getBaseContext(), "Room Added Successfully", Toast.LENGTH_SHORT).show();
                        firebaseRecyclerAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                supportFinishAfterTransition();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public static class RoomHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mRoomName, mRoomType;

        public RoomHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mRoomType = (TextView) itemView.findViewById(R.id.RoomType);
            mRoomName = (TextView) itemView.findViewById(R.id.RoomName);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, ChatRoom.class);
            intent.putExtra("Room Name", mRoomName.getText().toString());
            mContext.startActivity(intent);
        }
    }


}
