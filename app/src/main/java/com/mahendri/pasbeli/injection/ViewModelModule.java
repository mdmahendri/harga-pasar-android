package com.mahendri.pasbeli.injection;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.mahendri.pasbeli.ui.MapViewModel;
import com.mahendri.pasbeli.ui.harga.HargaViewModel;
import com.mahendri.pasbeli.ui.history.HistoryViewModel;
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
    abstract ViewModel bindHargaViewModel(HargaViewModel hargaViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel.class)
    abstract ViewModel bindMapViewModel(MapViewModel mapViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HistoryViewModel.class)
    abstract ViewModel bindHistoryViewModel (HistoryViewModel historyViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(PasBeliViewModelFactory factory);
}
