package com.mahendri.pasbeli.api;

import retrofit2.http.POST;

/**
 * @author Mahendri
 */

public interface WebService {

    @POST("api/harga")
    void sendHargaBaru();

}
