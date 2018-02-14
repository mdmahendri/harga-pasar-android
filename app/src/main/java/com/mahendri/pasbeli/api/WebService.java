package com.mahendri.pasbeli.api;

import com.mahendri.pasbeli.entity.HargaKomoditas;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @author Mahendri
 */

public interface WebService {

    @POST("api/harga")
    Call<String> sendHargaBaru(@Body List<HargaKomoditas> daftarHarga);

}
