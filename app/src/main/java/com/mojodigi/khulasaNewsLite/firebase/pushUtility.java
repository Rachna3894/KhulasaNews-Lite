package com.mojodigi.khulasaNewsLite.firebase;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class pushUtility {


public  static final String CLICK_PUSH_NOTIFICATION="click";
public  static  String NEWSURL;

public static final String UrlKey="key";

   //  its deprecated now    // public static final String API_PUSH_NOTIFICATION="http://development.bdigimedia.com/riccha_dev/khulasa-News/pushNotifications/setFcmToken.php";
  // public static final String API_PUSH_NOTIFICATION="http://onetracky.com/Apps/khulasa-Lite/pushNotifications/setFcmToken.php";
   public static final String API_PUSH_NOTIFICATION="http://onetracky.com/Apps/khulasa_lite/pushNotifications/setFcmToken.php";

    public static JSONObject prepareFcmJsonRequest(Context mContext, String deviceID , String nameOfDevice , String fcm_Token , String appVersion)
    {
        JSONObject object  =  new JSONObject();
        try {
            object.put("deviceId", deviceID);
            object.put("deviceName", nameOfDevice);
            object.put("fcmToken", fcm_Token);
            object.put("appVer", appVersion);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("Json request", object.toString());
        return object;
    }


    public static boolean checkIsOnline(Context mContext)
    {
        ConnectivityManager ConnectionManager=(ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()==true )
        {
            return true;

        }
        else
        {
            return false;

        }
    }


}
