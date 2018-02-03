package com.mahendri.pasbeli.injection;

import android.app.Application;

import com.mahendri.pasbeli.PasBeli;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

/**
 * @author Mahendri
 */

@Singleton
@Component(modules = {AndroidInjectionModule.class, AppModule.class})
public interface AppComponent {
    void inject(PasBeli pasBeli);

    @Component.Builder
    interface Builder {
        AppComponent build();
        @BindsInstance Builder application(Application application);
    }
}
