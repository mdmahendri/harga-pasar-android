package com.mahendri.pasbeli.repository;

import android.arch.lifecycle.LiveData;

import com.mahendri.pasbeli.database.HargaDao;
import com.mahendri.pasbeli.entity.HargaKomoditas;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Mahendri
 */
@Singleton
public class HargaRepository {

    private final HargaDao hargaDao;

    @Inject
    public HargaRepository(HargaDao hargaDao) {
        this.hargaDao = hargaDao;
    }

    public LiveData<List<HargaKomoditas>> getAllKomoditas() {
        return hargaDao.loadHargaKomoditasList();
    }

    public long insertNewEntry(HargaKomoditas komoditas) {
        return hargaDao.insert(komoditas);
    }

}
