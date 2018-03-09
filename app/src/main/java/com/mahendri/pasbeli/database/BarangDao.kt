package com.mahendri.pasbeli.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.mahendri.pasbeli.entity.Barang

/**
 * @author Mahendri
 */

@Dao
interface BarangDao {

    @Query("SELECT * FROM Barang")
    fun getBarang(): LiveData<List<Barang>>

    @Query("SELECT kualitas FROM Barang WHERE nama = :nama")
    fun getKualitas(nama: String): List<String>

    @Query("SELECT id_barang FROM Barang WHERE nama = :nama AND kualitas = :kualitas")
    fun getId(nama: String, kualitas: String): Int

    @Insert
    fun insertBarang(barang: Barang)
}