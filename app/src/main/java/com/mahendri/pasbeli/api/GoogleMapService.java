package com.mahendri.pasbeli.api;

import android.arch.lifecycle.LiveData;

import com.mahendri.pasbeli.api.map.PlaceNearbyResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author Mahendri
 */

public interface GoogleMapService {

    /*
     * response berisi tempat terdekat dari lokasi sekarang
     */
    @GET("api/place/nearbysearch/json?keyword=pasar%20traditional%20market&rankby=distance")
    LiveData<ApiResponse<PlaceNearbyResponse>> listNearbyMarket(@Query("key") String apiKey,
                              @Query("location") String location);

}
