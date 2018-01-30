package com.mahendri.hargapasar.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.mahendri.hargapasar.database.HargaKomoditasRepository;

/**
 * @author Mahendri
 */

public class CustomViewModelFactory implements ViewModelProvider.Factory {

    private final HargaKomoditasRepository repository;

    public CustomViewModelFactory(HargaKomoditasRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HargaKomoditasViewModel.class))
            return (T) new HargaKomoditasViewModel(repository);
        else
            throw new IllegalArgumentException("ViewModel Not Found");
    }
}
