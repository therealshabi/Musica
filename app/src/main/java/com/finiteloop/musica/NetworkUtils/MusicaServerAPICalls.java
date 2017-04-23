package com.finiteloop.musica.NetworkUtils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by therealshabi on 17/04/17.
 */

public abstract class MusicaServerAPICalls {
    private static final String SUCCESS_STATUS = "success";
    private static final String JSON_STATUS = "status";
    private static final String JSON_DATA = "data";

    private static final String SERVER_ADDRESS = "http://192.168.0.6:22222";
    private static final String USER_SIGNUP_POST_REQUEST = SERVER_ADDRESS + "/signup";
    private static final String USER_SEARCH_GET_REQUEST = SERVER_ADDRESS + "/user/search/";
    private static final String USER_EMAIL_GET_REQUEST = SERVER_ADDRESS + "/user/";

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

    public void searchUserRequest(Context context, String queryUser) {
        String queryURL = USER_SEARCH_GET_REQUEST + queryUser;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, queryURL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString(JSON_STATUS).equals(SUCCESS_STATUS)) {
                        isRequestSuccessful(true, response.getString(JSON_DATA));
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

    public void getUserOnEmail(Context context, String email) {
        String queryURL = USER_EMAIL_GET_REQUEST + email;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, queryURL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString(JSON_STATUS).equals(SUCCESS_STATUS)) {
                        isRequestSuccessful(true, response.getString(JSON_DATA));
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
}
