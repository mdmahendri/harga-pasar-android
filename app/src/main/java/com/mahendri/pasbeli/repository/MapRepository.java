package com.mahendri.pasbeli.repository;

import android.arch.lifecycle.LiveData;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mahendri.pasbeli.api.WebService;
import com.mahendri.pasbeli.viewmodel.AppExecutors;
import com.mahendri.pasbeli.api.ApiResponse;
import com.mahendri.pasbeli.database.PasBeliDb;
import com.mahendri.pasbeli.database.PasarDao;
import com.mahendri.pasbeli.entity.Pasar;
import com.mahendri.pasbeli.entity.Resource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Mahendri
 */

@Singleton
public class MapRepository {

    private final AppExecutors appExecutors;
    private final PasBeliDb pasBeliDb;
    private final PasarDao pasarDao;
    private final WebService webService;

    @Inject
    MapRepository(AppExecutors appExecutors, PasBeliDb pasBeliDb, PasarDao pasarDao,
                  WebService webService) {
        this.appExecutors = appExecutors;
        this.pasBeliDb = pasBeliDb;
        this.pasarDao = pasarDao;
        this.webService = webService;
    }

    public LiveData<Resource<List<Pasar>>> getNearbyPasar(Location location) {
        return new NetworkBoundResource<List<Pasar>, List<Pasar>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<Pasar> item) {
                pasBeliDb.beginTransaction();
                try {
                    for(Pasar pasar : item)
                        pasarDao.insertPasar(pasar);
                    pasBeliDb.setTransactionSuccessful();
                } finally {
                    pasBeliDb.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Pasar> data) {
                return data == null || data.size() < 1;
            }

            @NonNull
            @Override
            protected LiveData<List<Pasar>> loadFromDb() {
                return pasarDao.loadDaftarPasar(location.getLatitude(), location.getLatitude());
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Pasar>>> createCall() {
                String locationString = String.format("%s,%s", location.getLatitude(),
                        location.getLongitude());
                return webService.fetchPasarNear(locationString);
            }

        }.asLiveData();
    }
}
