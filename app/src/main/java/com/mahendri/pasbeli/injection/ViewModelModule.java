package com.mahendri.pasbeli.injection;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.mahendri.pasbeli.ui.harga.HargaViewModel;
import com.mahendri.pasbeli.viewmodel.PasBeliViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * @author Mahendri
 */

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(HargaViewModel.class)
    abstract ViewModel bindHargaKomoditasViewModel(HargaViewModel hargaViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(PasBeliViewModelFactory factory);
}
