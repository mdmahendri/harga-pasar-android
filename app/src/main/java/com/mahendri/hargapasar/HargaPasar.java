package com.mahendri.hargapasar;

import android.app.Application;

import timber.log.Timber;

/**
 * @author Mahendri
 */

public class HargaPasar extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
