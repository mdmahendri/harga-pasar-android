package com.mahendri.pasbeli.api;

import android.arch.lifecycle.LiveData;

import com.mahendri.pasbeli.entity.Barang;
import com.mahendri.pasbeli.entity.HargaKonsumen;

import java.util.List;

import io.reactivex.Completable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * @author Mahendri
 */

public interface WebService {

    @POST("api/harga")
    Completable sendHargaBaru(@Body List<HargaKonsumen> daftarHarga);

    @GET("api/barang")
    LiveData<ApiResponse<List<Barang>>> fetchListBarang();
}
