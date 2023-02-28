package com.mojodigi.khulasaNewsLite.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.Html;

import com.mojodigi.khulasaNewsLite.R;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MyNotificationManager {

    private Context mContext;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder mBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = "1001";
    public static final String NOTIFICATION_CHANNEL_NAME = "khulasaNews";

    public static final int ID_BIG_NOTIFICATION = 01;
    public static final int ID_SMALL_NOTIFICATION = 02;


    private Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


    public MyNotificationManager(Context mContext) {
        this.mContext = mContext;
    }


    public void showBigNotification(String title, String message, String url, Intent intent) {

        playNotificationSound();

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mContext,
                        ID_BIG_NOTIFICATION,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(getBitmapFromURL(url));

        mBuilder = new NotificationCompat.Builder(mContext , NOTIFICATION_CHANNEL_ID);

        Notification notification;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notification = mBuilder.setTicker(title).setWhen(0)
                    .setAutoCancel(true)
                    .setContentIntent(resultPendingIntent)
                    .setContentTitle(title)
                    .setStyle(bigPictureStyle)
                   .setSmallIcon(getNotificationIcon())
                     /* .setSmallIcon(R.drawable.ic_fcm_icon)
                     .setSmallIcon(R.mipmap.ic_fcm_launcher)*/
                    //.setColor(ContextCompat.getColor(mContext,R.color.colorAccent))
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher))
                    .setContentText(message)
                    .build();

        } else {
            notification = mBuilder.setTicker(title).setWhen(0)
                    .setAutoCancel(true)
                    .setContentIntent(resultPendingIntent)
                    .setContentTitle(title)
                    .setStyle(bigPictureStyle)
                    .setSmallIcon(R.drawable.ic_fcm_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher))
                    .setContentText(message)
                    .build();
        }


        notification.flags |= Notification.FLAG_AUTO_CANCEL;

         notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert notificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        assert notificationManager != null;
        notificationManager.notify(ID_BIG_NOTIFICATION /* Request Code */, mBuilder.build());
        //notificationManager.notify(ID_BIG_NOTIFICATION, notification);
    }

    //the method will show a small notification
    //parameters are title for message title, message for message text and an intent that will open
    //when you will tap on the notification
    public void showSmallNotification(String title, String message, Intent intent) {
        playNotificationSound();
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mContext,
                        ID_SMALL_NOTIFICATION,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        mBuilder = new NotificationCompat.Builder(mContext ,  NOTIFICATION_CHANNEL_ID);

        Notification notification;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = mBuilder.setTicker(title).setWhen(0)
                    .setAutoCancel(true)
                    .setContentIntent(resultPendingIntent)
                    .setContentTitle(title)
                    .setSmallIcon(getNotificationIcon())
                    /*.setSmallIcon(R.drawable.ic_fcm_icon)*/
                   // .setColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark))
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher))
                    .setContentText(message)
                    .build();

        } else {
            notification = mBuilder.setTicker(title).setWhen(0)
                    .setAutoCancel(true)
                    .setContentIntent(resultPendingIntent)
                    .setContentTitle(title)
                    .setSmallIcon(R.drawable.ic_fcm_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher))
                    .setContentText(message)
                    .build();
        }
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert notificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        assert notificationManager != null;
        notificationManager.notify(ID_SMALL_NOTIFICATION, notification);
    }




    //The method will return Bitmap from an image URL
    private Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    // Playing notification sound
    public void playNotificationSound() {
        try {
            Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.ic_fcm_icon : R.drawable.ic_fcm_icon;
    }

}