package com.mahendri.pasbeli.api

import android.arch.lifecycle.LiveData

import com.mahendri.pasbeli.entity.Barang
import com.mahendri.pasbeli.entity.HargaKonsumen
import com.mahendri.pasbeli.entity.Pasar

import javax.inject.Singleton

import io.reactivex.Completable
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * @author Mahendri
 */

interface WebService {

    @POST("harga")
    fun sendHargaBaru(@Body daftarHarga: List<HargaKonsumen>): Completable

    @POST("pasar")
    fun sendAddPasar(@Body daftarPasar: List<Pasar>): Call<String>

    @GET("barang")
    fun fetchListBarang(): LiveData<ApiResponse<List<Barang>>>

    @GET("pasar/{location}")
    fun fetchPasarNear(@Path("location") locationString: String): LiveData<ApiResponse<List<Pasar>>>
}
