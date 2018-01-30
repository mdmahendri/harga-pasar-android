package com.mahendri.hargapasar.injection;

import android.app.Application;

import dagger.Module;
import dagger.Provides;

/**
 * @author Mahendri
 */

@Module
public class AppModule {

    private final Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    Application provideApplication() {
        return application;
    }
}
