package com.mahendri.pasbeli.ui.harga;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import com.mahendri.pasbeli.entity.HargaKomoditas;
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

    LiveData<List<HargaKomoditas>> getListHargaKomoditas() {
        return repository.getAllKomoditas();
    }

    void insertNewHargaKomoditas(HargaKomoditas hargaKomoditas) {
        new InsertKomoditas().execute(hargaKomoditas);
    }

    class InsertKomoditas extends AsyncTask<HargaKomoditas, Void, Void> {

        @Override
        protected Void doInBackground(HargaKomoditas... hargaKomoditas) {
            repository.insertNewEntry(hargaKomoditas[0]);
            return null;
        }
    }
}
