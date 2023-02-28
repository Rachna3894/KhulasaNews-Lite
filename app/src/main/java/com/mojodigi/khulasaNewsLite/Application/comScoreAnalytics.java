package com.mojodigi.khulasaNewsLite.Application;

import android.app.Application;

import com.comscore.Analytics;
import com.comscore.PublisherConfiguration;

public class comScoreAnalytics extends Application {
    public void onCreate() {
        super.onCreate();

        PublisherConfiguration publisher = new PublisherConfiguration.Builder()
                .publisherId("37812790")
                .build();

        Analytics.getConfiguration().addClient(publisher);
        Analytics.getConfiguration().enableImplementationValidationMode();
        Analytics.start(getApplicationContext());
    }
}
