package com.mahendri.pasbeli.repository;

import android.arch.lifecycle.LiveData;

import com.mahendri.pasbeli.AppExecutors;
import com.mahendri.pasbeli.api.ApiResponse;
import com.mahendri.pasbeli.api.WebService;
import com.mahendri.pasbeli.database.HargaDao;
import com.mahendri.pasbeli.entity.HargaKomoditas;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

/**
 * @author Mahendri
 */
@Singleton
public class HargaRepository {

    private final AppExecutors appExecutors;
    private final HargaDao hargaDao;
    private final WebService webService;

    @Inject
    public HargaRepository(HargaDao hargaDao, WebService webService, AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
        this.hargaDao = hargaDao;
        this.webService = webService;
    }

    public LiveData<List<HargaKomoditas>> getAllKomoditas() {
        return hargaDao.loadHargaKomoditasList();
    }

    public void insertNewEntry(HargaKomoditas komoditas) {
        appExecutors.diskIO().execute(() -> {
            hargaDao.insert(komoditas);
        });
    }

    public Call<String> sendDataEntry() {
        return webService.sendHargaBaru(hargaDao.getUnsendDataHarga().getValue());
    }

}
