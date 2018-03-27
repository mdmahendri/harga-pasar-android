package com.mahendri.pasbeli.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

import com.mahendri.pasbeli.entity.Pasar

/**
 * @author Mahendri
 */

@Dao
interface PasarDao {

    @Query("SELECT * FROM Pasar ORDER BY ((latitude - :latUser) * (latitude - :latUser))"
            + " + ((longitude - :lngUser) * (longitude - :lngUser)) ASC"
            + " LIMIT 20")
    fun loadDaftarPasar(latUser: Double, lngUser: Double): LiveData<List<Pasar>>

    @Query("SELECT * FROM Pasar WHERE version = 0")
    fun addedLocallyList(): List<Pasar>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPasar(pasar: Pasar)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDaftarPasar(daftarPasar: List<Pasar>)

}
