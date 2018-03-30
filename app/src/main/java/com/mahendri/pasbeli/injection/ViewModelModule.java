package com.mahendri.pasbeli.injection;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.mahendri.pasbeli.ui.addharga.HargaViewModel;
import com.mahendri.pasbeli.ui.history.HistoryViewModel;
import com.mahendri.pasbeli.ui.main.MainViewModel;
import com.mahendri.pasbeli.ui.splash.SplashViewModel;
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
    @ViewModelKey(SplashViewModel.class)
    abstract ViewModel bindSplashViewModel(SplashViewModel splashViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel bindMainViewModel(MainViewModel mainViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HargaViewModel.class)
    abstract ViewModel bindHargaViewModel(HargaViewModel hargaViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HistoryViewModel.class)
    abstract ViewModel bindHistoryViewModel (HistoryViewModel historyViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(PasBeliViewModelFactory factory);
}
