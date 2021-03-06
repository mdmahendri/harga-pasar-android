package com.mahendri.pasbeli.injection;

import com.mahendri.pasbeli.ui.addharga.AddHargaActivity;
import com.mahendri.pasbeli.ui.addharga.AddHargaModule;
import com.mahendri.pasbeli.ui.history.DataHistoryActivity;
import com.mahendri.pasbeli.ui.main.MainActivity;
import com.mahendri.pasbeli.ui.main.MainModule;
import com.mahendri.pasbeli.ui.splash.SplashActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * @author Mahendri
 */

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector
    abstract SplashActivity bindSplashActivity();

    @ContributesAndroidInjector(modules = {MainModule.class})
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector(modules = {AddHargaModule.class})
    abstract AddHargaActivity bindAddHargaActivity();

    @ContributesAndroidInjector
    abstract DataHistoryActivity bindDataHistoryActivity();

}
