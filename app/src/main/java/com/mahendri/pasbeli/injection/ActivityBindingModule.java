package com.mahendri.pasbeli.injection;

import com.mahendri.pasbeli.ui.MainActivity;
import com.mahendri.pasbeli.ui.MainActivityModule;
import com.mahendri.pasbeli.ui.harga.AddKomoditiActivity;
import com.mahendri.pasbeli.ui.harga.DataHistoryActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * @author Mahendri
 */

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = {MainActivityModule.class})
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector
    abstract AddKomoditiActivity bindAddKomoditiActivity();

    @ContributesAndroidInjector
    abstract DataHistoryActivity bindDataHistoryActivity();

}
