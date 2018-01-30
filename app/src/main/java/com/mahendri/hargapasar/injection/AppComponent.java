package com.mahendri.hargapasar.injection;

import android.app.Application;

import com.mahendri.hargapasar.BaseActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Mahendri
 */

@Singleton
@Component(modules = {AppModule.class, RoomModule.class})
public interface AppComponent {
    void inject(BaseActivity baseActivity);
    Application application();
}
