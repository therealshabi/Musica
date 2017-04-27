package com.finiteloop.musica;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.finiteloop.musica.Models.UserModel;
import com.finiteloop.musica.NetworkUtils.MusicaServerAPICalls;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoveActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    public static String POST_ID="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.8));

        Bundle bundle = getIntent().getExtras();
        POST_ID = bundle.getString("POST_ID");

        recyclerView = (RecyclerView) findViewById(R.id.loveActivityRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        new MusicaServerAPICalls() {
            @Override
            public void isRequestSuccessful(boolean isSuccessful, String message) {
                if (isSuccessful) {
                    ArrayList<UserModel> users = new ArrayList<>();
                    try {
                        users.clear();
                        users = parseJsonResponse(message);
                        recyclerView.setAdapter(new RecyclerViewAdapter(getBaseContext(), users));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getBaseContext(), "There was an error while updating love user status...", Toast.LENGTH_SHORT).show();
                }
            }
        }.getUserWhoLovedPost(getBaseContext(),POST_ID);

    }

    public ArrayList<UserModel> parseJsonResponse(String response) throws JSONException {

        ArrayList<UserModel> arrayList = new ArrayList<>();
        JSONObject root = new JSONObject(response);
        JSONArray data = root.getJSONArray("data");
        for(int i = 0; i < data.length(); i++){
            JSONObject user = data.getJSONObject(i);
            UserModel userModel = new UserModel();
            userModel.setUsername(user.getString("user_name"));
            userModel.setProfilePicUrl(user.getString("profile_pic"));
            arrayList.add(userModel);
        }
        return arrayList;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CircularImageView userProfilePic;
        TextView username;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            userProfilePic = (CircularImageView) itemView.findViewById(R.id.activity_like_card_view_image);
            username = (TextView) itemView.findViewById(R.id.activity_like_card_view_username_text);
        }

        @Override
        public void onClick(View view) {
        }
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder>{

        Context mContext;
        private ArrayList<UserModel> likeList;

        public RecyclerViewAdapter(Context context, ArrayList<UserModel> likeList) {
            mContext = context;
            this.likeList=likeList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.activity_like_card_view,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            if(likeList.size()!=0){
                UserModel userModel = likeList.get(position);
                holder.username.setText(userModel.getUsername());
                if(userModel.getProfilePicUrl().isEmpty())
                {
                    holder.userProfilePic.setImageResource(R.drawable.mountain_pic2);
                }
                else{
                    Picasso.with(getBaseContext()).load(Uri.parse(userModel.getProfilePicUrl())).into(holder.userProfilePic);
                }
            }
        }

        @Override
        public int getItemCount() {
            return likeList.size();
        }
    }


}
