package com.mahendri.pasbeli.api;

import com.mahendri.pasbeli.entity.map.PlaceNearbyResponse;

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
    @GET("api/place/nearbysearch/json?keyword=pasar%20traditional%20market&language=id&rankby=distance")
    Call<PlaceNearbyResponse> listNearbyMarket(@Query("key") String apiKey,
                                               @Query("location") String location);

}
