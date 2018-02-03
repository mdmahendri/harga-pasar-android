package com.mahendri.pasbeli.repository;

import android.location.Location;
import android.support.annotation.NonNull;

import com.mahendri.pasbeli.api.GoogleMapService;
import com.mahendri.pasbeli.api.map.PlaceNearbyResponse;
import com.mahendri.pasbeli.api.map.PlaceResult;
import com.mahendri.pasbeli.preference.Constants;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * @author Mahendri
 */

@Singleton
public class MapRepository {

    private final GoogleMapService googleMapService;
    private List<PlaceResult> placeResults;

    @Inject
    MapRepository(GoogleMapService googleMapService) {
        this.googleMapService = googleMapService;
    }

    public List<PlaceResult> getNearbyPasar(Location location) {
        String locationString = String.format("%s,%s", location.getLatitude(), location.getLongitude());

        googleMapService.listNearbyMarket(Constants.GOOGLE_API_KEY, locationString)
                .enqueue(new Callback<PlaceNearbyResponse>() {
            @Override
            public void onResponse(@NonNull Call<PlaceNearbyResponse> call,
                                   @NonNull Response<PlaceNearbyResponse> response) {
                if (!response.isSuccessful()) {
                    Timber.w("respon error");
                    return;
                }

                PlaceNearbyResponse nearbyResponse = response.body();
                Timber.i("Place name: %s", nearbyResponse.getStatus());
                placeResults =  nearbyResponse.getResults();
            }

            @Override
            public void onFailure(Call<PlaceNearbyResponse> call, Throwable t) {
                Timber.w(t);
            }
        });
        return placeResults;
    }
}
