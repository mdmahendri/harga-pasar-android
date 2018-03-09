package com.mahendri.pasbeli.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mahendri.pasbeli.AppExecutors;
import com.mahendri.pasbeli.api.ApiResponse;
import com.mahendri.pasbeli.api.WebService;
import com.mahendri.pasbeli.database.BarangDao;
import com.mahendri.pasbeli.database.HargaDao;
import com.mahendri.pasbeli.database.PasBeliDb;
import com.mahendri.pasbeli.entity.Barang;
import com.mahendri.pasbeli.entity.HargaKonsumen;
import com.mahendri.pasbeli.entity.Resource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Maybe;
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
    public HargaRepository(HargaDao hargaDao, PasBeliDb pasBeliDb, BarangDao barangDao,
                           WebService webService, AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
        this.pasBeliDb = pasBeliDb;
        this.hargaDao = hargaDao;
        this.barangDao = barangDao;
        this.webService = webService;
    }

    public LiveData<List<HargaKonsumen>> getAllKomoditas() {
        return hargaDao.loadHargaKomoditasList();
    }

    public LiveData<Resource<List<Barang>>> getAllBarang() {
        return new NetworkBoundResource<List<Barang>, List<Barang>>(appExecutors) {

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
            protected boolean shouldFetch(@Nullable List<Barang> data) {
                return data == null || data.size() == 0;
            }

            @NonNull
            @Override
            protected LiveData<List<Barang>> loadFromDb() {
                return barangDao.getBarang();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Barang>>> createCall() {
                return webService.fetchListBarang();
            }
        }.asLiveData();
    }

    public List<String> getKualitas(String nama) {
        return barangDao.getKualitas(nama);
    }

    public int getIdBarang(String nama, String kualitas) {
        return barangDao.getId(nama, kualitas);
    }

    public void insertNewEntry(int idBarang,long harga,String namaTempat,double latitude,double longitude) {
        HargaKonsumen hargaKonsumen = new HargaKonsumen();
        hargaKonsumen.idBarang = idBarang;
        hargaKonsumen.hargaBarang = harga;
        hargaKonsumen.namaTempat = namaTempat;
        hargaKonsumen.latitude = latitude;
        hargaKonsumen.longitude = longitude;
        hargaKonsumen.waktuCatat = System.currentTimeMillis();

        appExecutors.diskIO().execute(() -> hargaDao.insertHarga(hargaKonsumen));
    }

    public Completable sendDataEntry() {
        return Maybe.fromCallable(hargaDao::getUnsendDataHarga).flatMapCompletable(hargaKonsumen -> {
            Timber.i("array ada isinya: %s", hargaKonsumen != null && hargaKonsumen.size() != 0);
            if (hargaKonsumen == null || hargaKonsumen.size() == 0 )
                throw new Exception("Daftar entri kosong.");
            else return webService.sendHargaBaru(hargaKonsumen);
        }).doOnComplete(hargaDao::updateHarga);
    }
}