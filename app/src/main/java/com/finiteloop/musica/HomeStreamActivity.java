package com.finiteloop.musica;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeStreamActivity extends AppCompatActivity {

    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_stream);

        recyclerView=(RecyclerView)findViewById(R.id.home_stream_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recyclerView.setAdapter(new RecyclerViewAdapter());

    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder>{

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(getBaseContext()).inflate(R.layout.homestream_card_view,parent,false);
            return new RecyclerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }
    public class RecyclerViewHolder extends RecyclerView.ViewHolder{

        public RecyclerViewHolder(View itemView) {
            super(itemView);
        }
    }
}
