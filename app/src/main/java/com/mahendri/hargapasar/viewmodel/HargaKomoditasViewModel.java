package com.mahendri.hargapasar.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.mahendri.hargapasar.database.HargaKomoditasRepository;
import com.mahendri.hargapasar.entity.data.HargaKomoditas;

import java.util.List;

/**
 * @author Mahendri
 */

public class HargaKomoditasViewModel extends ViewModel {

    private HargaKomoditasRepository repository;

    HargaKomoditasViewModel(HargaKomoditasRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<HargaKomoditas>> getListHargaKomoditas() {
        return repository.getAllKomoditas();
    }
}
