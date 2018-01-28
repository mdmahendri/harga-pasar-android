package com.mahendri.hargapasar.network;

import android.location.Location;

import com.google.android.gms.maps.model.MarkerOptions;
import com.mahendri.hargapasar.Constants;
import com.mahendri.hargapasar.entity.map.PlaceNearbyResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Mahendri
 */

public class RetrofitConnect {

    public static Call<PlaceNearbyResponse> retrieveNearby(Location location) {
        String mapUrl = Constants.MAP_WEB_SERVICE;
        String locationString = String.format("%s,%s", location.getLatitude(), location.getLongitude());

        Retrofit mapRetrofit = new Retrofit.Builder()
                .baseUrl(mapUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GoogleMapService mapService  = mapRetrofit.create(GoogleMapService.class);

        return mapService.listNearbyMarket(Constants.GOOGLE_API_KEY, locationString);
    }
}
