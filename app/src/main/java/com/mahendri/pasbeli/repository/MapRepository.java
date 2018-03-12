package com.mahendri.pasbeli.repository;

import android.arch.lifecycle.LiveData;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mahendri.pasbeli.viewmodel.AppExecutors;
import com.mahendri.pasbeli.api.ApiResponse;
import com.mahendri.pasbeli.api.GoogleMapService;
import com.mahendri.pasbeli.api.map.PlaceNearbyResponse;
import com.mahendri.pasbeli.api.map.PlaceResult;
import com.mahendri.pasbeli.database.PasBeliDb;
import com.mahendri.pasbeli.database.PasarDao;
import com.mahendri.pasbeli.entity.Pasar;
import com.mahendri.pasbeli.entity.Resource;
import com.mahendri.pasbeli.preference.Constants;

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
    private final GoogleMapService googleMapService;

    @Inject
    MapRepository(AppExecutors appExecutors, PasBeliDb pasBeliDb, PasarDao pasarDao,
                  GoogleMapService googleMapService) {
        this.appExecutors = appExecutors;
        this.pasBeliDb = pasBeliDb;
        this.pasarDao = pasarDao;
        this.googleMapService = googleMapService;
    }

    public LiveData<Resource<List<Pasar>>> getNearbyPasar(Location location) {
        return new NetworkBoundResource<List<Pasar>, PlaceNearbyResponse>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull PlaceNearbyResponse item) {
                pasBeliDb.beginTransaction();
                try {
                    for(PlaceResult place : item.getResults())
                        pasarDao.insertPasar(new Pasar(place));
                    pasBeliDb.setTransactionSuccessful();
                } finally {
                    pasBeliDb.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Pasar> data) {
                return data == null || data.size() < 20;
            }

            @NonNull
            @Override
            protected LiveData<List<Pasar>> loadFromDb() {
                return pasarDao.loadDaftarPasar(location.getLatitude(), location.getLatitude());
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<PlaceNearbyResponse>> createCall() {
                String locationString = String.format("%s,%s", location.getLatitude(),
                        location.getLongitude());
                return googleMapService.listNearbyMarket(Constants.GOOGLE_API_KEY, locationString);
            }

        }.asLiveData();
    }
}
