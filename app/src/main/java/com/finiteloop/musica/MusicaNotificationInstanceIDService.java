package com.finiteloop.musica;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MusicaNotificationInstanceIDService extends FirebaseInstanceIdService {
    final static String TAG = "Notificas";


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String token = FirebaseInstanceId.getInstance().getToken();

        Log.d(TAG, "token is " + token);

    }
}
