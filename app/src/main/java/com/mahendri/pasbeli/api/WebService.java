package com.mahendri.pasbeli.api;

import android.arch.lifecycle.LiveData;

import com.mahendri.pasbeli.entity.Barang;
import com.mahendri.pasbeli.entity.HargaKonsumen;
import com.mahendri.pasbeli.entity.Pasar;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Completable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author Mahendri
 */

@Singleton
public interface WebService {

    @POST("harga")
    Completable sendHargaBaru(@Body List<HargaKonsumen> daftarHarga);

    @POST("pasar")
    Call<String> sendAddPasar(@Body List<Pasar> daftarPasar);

    @GET("barang")
    LiveData<ApiResponse<List<Barang>>> fetchListBarang();

    @GET("pasar/{location}")
    LiveData<ApiResponse<List<Pasar>>> fetchPasarNear(@Path("location") String locationString);
}
