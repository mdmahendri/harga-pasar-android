package com.mahendri.pasbeli.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.mahendri.pasbeli.entity.BarangHarga
import com.mahendri.pasbeli.entity.HargaKonsumen

/**
 * @author Mahendri
 */

@Dao
interface HargaDao {

    @get:Query("SELECT * FROM HargaKonsumen WHERE uploaded = 0")
    val unsendDataHarga: List<HargaKonsumen>

    @Query("SELECT Barang.nama, HargaKonsumen.harga, HargaKonsumen.nama_tempat," +
            " HargaKonsumen.waktu_catat FROM HargaKonsumen" +
            " JOIN Barang ON HargaKonsumen.id_barang = Barang.id_barang")
    fun loadCatatanHarga(): LiveData<List<BarangHarga>>

    @Insert
    fun insertHarga(harga: HargaKonsumen): Long

    @Query("UPDATE HargaKonsumen SET uploaded = 1 WHERE uploaded = 0")
    fun updateHarga()
}
