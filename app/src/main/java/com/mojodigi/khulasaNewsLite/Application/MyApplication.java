package com.mojodigi.khulasaNewsLite.Application;

import android.app.Application;

//import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.mojodigi.khulasaNewsLite.AddsUtility.AddConstants;
import com.mojodigi.khulasaNewsLite.AddsUtility.SharedPreferenceUtil;
import com.mojodigi.khulasaNewsLite.Analytics.AnalyticsTrackers;
import com.mojodigi.khulasaNewsLite.R;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;


public class MyApplication extends android.support.multidex.MultiDexApplication {
    public static final String TAG = MyApplication.class
            .getSimpleName();

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        // MobileAds.initialize(this, "ca-app-pub-8509384168493764~9766841905"); //demo app id
       //MobileAds.initialize(this, getResources().getString(R.string.admob_app_id)); //actual app id

        SharedPreferenceUtil addPref=new SharedPreferenceUtil(getApplicationContext());
        String appId=addPref.getStringValue(AddConstants.APP_ID, AddConstants.NOT_FOUND);
        if(appId !=null && !appId.equalsIgnoreCase(AddConstants.NOT_FOUND) )
            MobileAds.initialize(this, appId); //actual app id


        AnalyticsTrackers.initialize(this);
        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);



        //for fb adds
        //https://developers.facebook.com/docs/audience-network/reference/android/com/facebook/ads/audiencenetworkads.html/
       // AudienceNetworkAds.initialize(getApplicationContext());
      //  AudienceNetworkAds.isInAdsProcess(getApplicationContext());



        //App mertrica sdk

        // Creating an extended library configuration.
        //key for khulasaLite
        YandexMetricaConfig config = YandexMetricaConfig.newConfigBuilder("f6581e6c-78e4-466f-b9a0-aa2a8d1be5d5").build();
        // Initializing the AppMetrica SDK.
        YandexMetrica.activate(getApplicationContext(), config);
        // Automatic tracking of user activity.
        YandexMetrica.enableActivityAutoTracking(this);

        // Automatic tracking user activity.
        YandexMetrica.enableActivityAutoTracking(this);
        //App mertrica sdk
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public synchronized Tracker getGoogleAnalyticsTracker() {
        AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
        return analyticsTrackers.get(AnalyticsTrackers.Target.APP);
    }

    /***
     * Tracking screen view
     *
     * @param screenName screen name to be displayed on GA dashboard
     */
    public void trackScreenView(String screenName) {
        Tracker t = getGoogleAnalyticsTracker();

        // Set screen name.
        t.setScreenName(screenName);

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());

        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }

    /***
     * Tracking exception
     *
     * @param e exception to be tracked
     */
    public void trackException(Exception e) {
        if (e != null) {
            Tracker t = getGoogleAnalyticsTracker();

            t.send(new HitBuilders.ExceptionBuilder()
                    .setDescription(
                            new StandardExceptionParser(this, null)
                                    .getDescription(Thread.currentThread().getName(), e))
                    .setFatal(false)
                    .build()
            );
        }
    }

    /***
     * Tracking event
     *
     * @param category event category
     * @param action   action of the event
     * @param label    label
     */
    public void trackEvent(String category, String action, String label) {
        Tracker t = getGoogleAnalyticsTracker();

        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
    }

}
