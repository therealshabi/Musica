package com.finiteloop.musica.NetworkUtils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.finiteloop.musica.Models.PostModel;
import com.finiteloop.musica.SharedPreferencesUtils.UserDataSharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by therealshabi on 17/04/17.
 */

public abstract class MusicaServerAPICalls {
    private static final String SUCCESS_STATUS = "success";
    private static final String JSON_STATUS = "status";

    private static final String SERVER_ADDRESS = "http://192.168.1.110:22222";
    private static final String USER_SIGNUP_POST_REQUEST = SERVER_ADDRESS + "/signup";
    private static final String USER_POST_REQUEST = SERVER_ADDRESS + "/posts";
    private static String USER_HOMESTREAM_POST_REQUEST = SERVER_ADDRESS + "/user/post/homestream/";

    public ArrayList<PostModel> homeStreamPostArrayList;

    public abstract void isRequestSuccessful(boolean isSuccessful, String message);

    public void submitUserSignupDetails(Context context, JSONObject user) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, USER_SIGNUP_POST_REQUEST, user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString(JSON_STATUS).equals(SUCCESS_STATUS)) {
                        isRequestSuccessful(true, SUCCESS_STATUS);
                    } else {
                        isRequestSuccessful(false, response.getString(JSON_STATUS));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    isRequestSuccessful(false, null);
                    Log.d("Exception", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isRequestSuccessful(false, null);
            }
        });

        Volley.newRequestQueue(context).add(request);
    }

    public void submitPostByUser(Context context, JSONObject post){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, USER_POST_REQUEST, post, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString(JSON_STATUS).equals(SUCCESS_STATUS)) {
                        isRequestSuccessful(true, SUCCESS_STATUS);
                    } else {
                        isRequestSuccessful(false, response.getString(JSON_STATUS));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    isRequestSuccessful(false, null);
                    Log.d("Exception", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isRequestSuccessful(false,null);
            }
        });
        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }

    public void getHomeStreamPostOfUser(final Context context){
        USER_HOMESTREAM_POST_REQUEST += UserDataSharedPreference.getEmail(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, USER_HOMESTREAM_POST_REQUEST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("RESPONSE_TEXT", response);
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString(JSON_STATUS).equals(SUCCESS_STATUS)) {
                        isRequestSuccessful(true, response);
                    } else {
                        isRequestSuccessful(false, obj.getString(JSON_STATUS));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Error while fetching post from the database",Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

}
