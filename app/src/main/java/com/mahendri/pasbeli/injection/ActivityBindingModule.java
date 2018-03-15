package com.mahendri.pasbeli.injection;

import com.mahendri.pasbeli.ui.main.MainActivity;
import com.mahendri.pasbeli.ui.main.MainModule;
import com.mahendri.pasbeli.ui.addharga.AddHargaActivity;
import com.mahendri.pasbeli.ui.history.DataHistoryActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * @author Mahendri
 */

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = {MainModule.class})
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector
    abstract AddHargaActivity bindAddHargaActivity();

    @ContributesAndroidInjector
    abstract DataHistoryActivity bindDataHistoryActivity();

}
