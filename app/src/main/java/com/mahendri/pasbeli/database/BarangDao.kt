package com.mahendri.pasbeli.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.mahendri.pasbeli.entity.Barang
import com.mahendri.pasbeli.entity.KualitasUnit

/**
 * @author Mahendri
 */

@Dao
interface BarangDao {

    @Query("SELECT DISTINCT nama FROM Barang")
    fun getDistinctBarang(): LiveData<List<String>>

    @Query("SELECT kualitas, satuan FROM Barang WHERE nama = :nama")
    fun getKualitasUnit(nama: String): List<KualitasUnit>

    @Query("SELECT id_barang FROM Barang WHERE nama = :nama AND kualitas = :kualitas")
    fun getId(nama: String, kualitas: String): Int

    @Insert
    fun insertBarang(barang: Barang)
}