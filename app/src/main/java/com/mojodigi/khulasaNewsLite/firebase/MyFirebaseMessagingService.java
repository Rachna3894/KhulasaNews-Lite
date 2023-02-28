package com.mojodigi.khulasaNewsLite.firebase;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mojodigi.khulasaNewsLite.AddsUtility.SharedPreferenceUtil;
import com.mojodigi.khulasaNewsLite.WebviewActivity;
import com.mojodigi.khulasaNewsLite.WebviewActivity_Notification;


import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private SharedPreferenceUtil addprefs;
    private Context mContext;

    @Override
    public void onNewToken(String refreshedToken) {
        super.onNewToken(refreshedToken);
        System.out.print("Get Token : " + refreshedToken);
        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);

        if (mContext == null) {
            mContext = MyFirebaseMessagingService.this;
        }
        if (addprefs == null) {
            addprefs = new SharedPreferenceUtil(mContext);
        }
    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e(TAG, "send To Server: " + token);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (mContext == null) {
            mContext = MyFirebaseMessagingService.this;
        }
        if (addprefs == null) {
            addprefs = new SharedPreferenceUtil(mContext);
        }

        if (remoteMessage.getData().size() > 0) {
            //Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                sendPushNotification(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }


    private void sendPushNotification(JSONObject json) {
        //optionally we can display the json into log
        Log.e(TAG, "Notification JSON " + json.toString());
        try {
            //getting the json data
            JSONObject data = json.getJSONObject("data");
            //parsing json data
            String title = data.getString("title");
            String isBackground = data.getString("is_background");
            String message = data.getString("message");
            String imageUrl = data.getString("image");
            String webUrl = data.getString("url");
            String packageName = data.getString("packageName");
            //String timeStamp = data.getString("timestamp");
            Log.e("packageName " , packageName);


            if(packageName.equals("com.mojodigi.khulasaNewsLite")){
                //Log.e("packageName " , "Package Name are equals");
                //addprefs.setValue(pushUtility.CLICK_PUSH_NOTIFICATION, "true");
                pushUtility.NEWSURL = webUrl;

            }
            //creating MyNotificationManager object
            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());

            //creating an intent for the notification


            Intent intent = new Intent(getApplicationContext(), WebviewActivity_Notification.class);
            intent.putExtra(pushUtility.UrlKey, webUrl);



            //if there is no image
            if(imageUrl.equals("null")){
                //displaying small notification
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mNotificationManager.showSmallNotification(title, message, intent);
            }else{
                //if there is an image
                //displaying a big notification
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mNotificationManager.showBigNotification(title, message, imageUrl, intent);
            }

        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }




}