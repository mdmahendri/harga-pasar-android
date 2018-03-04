package com.mahendri.pasbeli.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Query
import com.mahendri.pasbeli.entity.Barang

/**
 * @author Mahendri
 */
interface BarangDao {

    @Query("SELECT * FROM Barang")
    fun getBarang(): LiveData<Barang>
}