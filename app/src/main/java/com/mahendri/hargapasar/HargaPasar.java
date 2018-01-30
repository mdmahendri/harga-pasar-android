package com.mahendri.hargapasar;

import android.app.Application;

import com.mahendri.hargapasar.injection.AppComponent;
import com.mahendri.hargapasar.injection.AppModule;
import com.mahendri.hargapasar.injection.DaggerAppComponent;
import com.mahendri.hargapasar.injection.RoomModule;

import timber.log.Timber;

/**
 * @author Mahendri
 */

public class HargaPasar extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .roomModule(new RoomModule(this))
                .build();
    }
}
