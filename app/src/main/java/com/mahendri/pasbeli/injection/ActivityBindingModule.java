package com.mahendri.pasbeli.injection;

import com.mahendri.pasbeli.ui.MainActivity;
import com.mahendri.pasbeli.ui.MainModule;
import com.mahendri.pasbeli.ui.harga.AddHargaActivity;
import com.mahendri.pasbeli.ui.harga.DataHistoryActivity;

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
    abstract AddHargaActivity bindAddKomoditiActivity();

    @ContributesAndroidInjector
    abstract DataHistoryActivity bindDataHistoryActivity();

}
