package com.mahendri.hargapasar.database;

import android.arch.lifecycle.LiveData;

import com.mahendri.hargapasar.entity.data.HargaKomoditas;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Mahendri
 */

public class HargaKomoditasRepository {

    private final HargaKomoditasDao hargaKomoditasDao;

    @Inject
    public HargaKomoditasRepository(HargaKomoditasDao hargaKomoditasDao) {
        this.hargaKomoditasDao = hargaKomoditasDao;
    }

    public LiveData<List<HargaKomoditas>> getAllKomoditas() {
        return hargaKomoditasDao.loadHargaKomoditasList();
    }

}
