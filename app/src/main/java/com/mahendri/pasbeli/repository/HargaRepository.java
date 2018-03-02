package com.mahendri.pasbeli.repository;

import android.arch.lifecycle.LiveData;

import com.mahendri.pasbeli.AppExecutors;
import com.mahendri.pasbeli.api.WebService;
import com.mahendri.pasbeli.database.HargaDao;
import com.mahendri.pasbeli.entity.HargaKonsumen;

import java.util.EmptyStackException;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import retrofit2.HttpException;
import timber.log.Timber;

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

    public LiveData<List<HargaKonsumen>> getAllKomoditas() {
        return hargaDao.loadHargaKomoditasList();
    }

    public void insertNewEntry(HargaKonsumen komoditas) {
        appExecutors.diskIO().execute(() -> hargaDao.insertHarga(komoditas));
    }

    public Completable sendDataEntry() {
        return Maybe.fromCallable(hargaDao::getUnsendDataHarga).flatMapCompletable(hargaKonsumen -> {
            Timber.i("array ada isinya: %s", hargaKonsumen != null && hargaKonsumen.size() != 0);
            if (hargaKonsumen == null || hargaKonsumen.size() == 0 )
                throw new Exception("daftar entri kosong");
            else return webService.sendHargaBaru(hargaKonsumen);
        }).doOnComplete(hargaDao::updateHarga);
    }
}