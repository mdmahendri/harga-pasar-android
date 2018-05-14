package com.mahendri.pasbeli.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mahendri.pasbeli.api.ApiResponse;
import com.mahendri.pasbeli.api.WebService;
import com.mahendri.pasbeli.database.BarangDao;
import com.mahendri.pasbeli.database.HargaDao;
import com.mahendri.pasbeli.database.PasBeliDb;
import com.mahendri.pasbeli.entity.Barang;
import com.mahendri.pasbeli.entity.BarangHarga;
import com.mahendri.pasbeli.entity.HargaKonsumen;
import com.mahendri.pasbeli.entity.KualitasUnit;
import com.mahendri.pasbeli.entity.Resource;
import com.mahendri.pasbeli.viewmodel.AppExecutors;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author Mahendri
 */
@Singleton
public class HargaRepository {

    private final AppExecutors appExecutors;
    private final PasBeliDb pasBeliDb;
    private final HargaDao hargaDao;
    private final BarangDao barangDao;
    private final WebService webService;

    @Inject
    HargaRepository(HargaDao hargaDao, PasBeliDb pasBeliDb, BarangDao barangDao,
                           WebService webService, AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
        this.pasBeliDb = pasBeliDb;
        this.hargaDao = hargaDao;
        this.barangDao = barangDao;
        this.webService = webService;
    }

    public LiveData<List<BarangHarga>> getCatatan() {
        return hargaDao.loadCatatanHarga();
    }

    public LiveData<Resource<List<String>>> getAllBarang() {
        return new NetworkBoundResource<List<String>, List<Barang>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<Barang> item) {
                pasBeliDb.beginTransaction();
                try {
                    for (Barang barang : item)
                        barangDao.insertBarang(barang);
                    pasBeliDb.setTransactionSuccessful();
                } finally {
                    pasBeliDb.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<String> data) {
                return data == null || data.size() == 0;
            }

            @NonNull
            @Override
            protected LiveData<List<String>> loadFromDb() {
                return barangDao.getDistinctBarang();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Barang>>> createCall() {
                return webService.fetchListBarang();
            }
        }.asLiveData();
    }

    public List<KualitasUnit> getKualitas(String nama) {
        return barangDao.getKualitasUnit(nama);
    }

    public int getIdBarang(String nama, String kualitas) {
        return barangDao.getId(nama, kualitas);
    }

    public void insertNewEntry(String mail, int idBarang, long harga, String namaTempat,
                               double latitude, double longitude) {
        HargaKonsumen hargaKonsumen = new HargaKonsumen(
                idBarang, harga, System.currentTimeMillis(), namaTempat, mail, latitude, longitude
        );

        appExecutors.diskIO().execute(() -> hargaDao.insertHarga(hargaKonsumen));
    }

    public Completable sendDataEntry() {
        return Maybe
                .fromCallable(hargaDao::getUnsendDataHarga)
                .flatMapCompletable(hargaKonsumen -> {
                    Timber.i("array ada isinya: %s", hargaKonsumen != null
                            && hargaKonsumen.size() != 0);
                    if (hargaKonsumen == null || hargaKonsumen.size() == 0 )
                        throw new Exception("Daftar entri kosong.");
                    else
                        return webService.sendHargaBaru(hargaKonsumen);
                })
                .doOnComplete(hargaDao::updateHarga)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}