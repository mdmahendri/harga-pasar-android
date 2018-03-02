package com.mahendri.pasbeli.ui.harga;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.mahendri.pasbeli.entity.HargaKonsumen;
import com.mahendri.pasbeli.repository.HargaRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Mahendri
 */

public class HargaViewModel extends ViewModel {

    private HargaRepository repository;

    @Inject
    HargaViewModel(HargaRepository repository) {
        this.repository = repository;
    }

    LiveData<List<HargaKonsumen>> getListHargaKomoditas() {
        return repository.getAllKomoditas();
    }

    void insertNewHargaKomoditas(HargaKonsumen hargaKonsumen) {
        repository.insertNewEntry(hargaKonsumen);
    }
}
