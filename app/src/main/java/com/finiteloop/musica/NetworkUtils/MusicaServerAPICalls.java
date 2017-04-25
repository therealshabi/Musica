package com.finiteloop.musica.NetworkUtils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.finiteloop.musica.Models.PostModel;
import com.finiteloop.musica.SharedPreferencesUtils.UserDataSharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by therealshabi on 17/04/17.
 */

public abstract class MusicaServerAPICalls {
    private static final String SUCCESS_STATUS = "success";
    private static final String JSON_STATUS = "status";
    private static final String JSON_DATA = "data";

    private static final String SERVER_ADDRESS = "http://192.168.1.110:22222";
    private static final String USER_SIGNUP_POST_REQUEST = SERVER_ADDRESS + "/signup";
    private static final String USER_SEARCH_GET_REQUEST = SERVER_ADDRESS + "/user/search/";
    private static final String USER_EMAIL_GET_REQUEST = SERVER_ADDRESS + "/user/";
    private static final String USER_HOMESTREAM_POST_REQUEST = SERVER_ADDRESS + "/user/homeStreamPost/";
    private static final String USER_POST_REQUEST = SERVER_ADDRESS + "/posts";
    private static final String POST_LIKE_REQUEST = SERVER_ADDRESS + "/post/like/";
    private static final String POST_LOVE_REQUEST = SERVER_ADDRESS + "/post/love/";
    private static final String POST_UNLIKE_REQUEST = SERVER_ADDRESS + "/post/unlike/";
    private static final String POST_UNLOVE_REQUEST = SERVER_ADDRESS + "/post/unlove/";
    private static final String GET_EMAIL_POST_LIKE_REQUEST = SERVER_ADDRESS + "/user/post/like/email/";
    private static final String GET_EMAIL_POST_LOVE_REQUEST = SERVER_ADDRESS + "/user/post/love/email/";



    //public ArrayList<PostModel> homeStreamPostArrayList;

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
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
                isRequestSuccessful(false, null);
            }
        });

        Volley.newRequestQueue(context).add(jsonObjectRequest);
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
                    isRequestSuccessful(false, null);
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

    public void getHomeStreamPostOfUser(final Context context){
        String query = USER_HOMESTREAM_POST_REQUEST + UserDataSharedPreference.getEmail(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, query, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
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
                isRequestSuccessful(false, null);
                Toast.makeText(context,"Error while fetching post from the database " + error,Toast.LENGTH_SHORT).show();
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }


    public void submitLikeByUser(Context context, JSONObject details,String id){
        String query = POST_LIKE_REQUEST + id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, query, details, new Response.Listener<JSONObject>() {
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

        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }

    public void submitLoveByUser(Context context, JSONObject details,String id){
        String query = POST_LOVE_REQUEST + id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, query, details, new Response.Listener<JSONObject>() {
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

        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }


    public void submitUnLikeByUser(Context context, JSONObject details,String id){
        String query = POST_UNLIKE_REQUEST + id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, query, details, new Response.Listener<JSONObject>() {
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

        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }

    public void submitUnLoveByUser(Context context, JSONObject details,String id){
        String query = POST_UNLOVE_REQUEST + id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, query, details, new Response.Listener<JSONObject>() {
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

        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }

    public void getEmailOfUserWhoLikedPost(final Context context, String id){
        String query = GET_EMAIL_POST_LIKE_REQUEST + id;
        Log.d("URL: ",query);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, query, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
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
                isRequestSuccessful(false, null);
                Toast.makeText(context,"Error while fetching email from the database " + error,Toast.LENGTH_SHORT).show();
                Log.d("Error is : ",error+"");
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }


    public void getEmailOfUserWhoLovedPost(final Context context, String id){
        String query = GET_EMAIL_POST_LOVE_REQUEST + id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, query, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
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
                isRequestSuccessful(false, null);
                Toast.makeText(context,"Error while fetching email from the database " + error,Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }


}
