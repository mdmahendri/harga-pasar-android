package com.mahendri.pasbeli.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.mahendri.pasbeli.entity.HargaKonsumen;

import java.util.List;

/**
 * @author Mahendri
 */

@Dao
public interface HargaDao {

    @Query("SELECT * FROM HargaKonsumen")
    LiveData<List<HargaKonsumen>> loadHargaKomoditasList();

    @Query("SELECT * FROM HargaKonsumen WHERE uploaded = 0")
    List<HargaKonsumen> getUnsendDataHarga();

    @Insert
    long insertHarga(HargaKonsumen harga);

    @Query("UPDATE HargaKonsumen SET uploaded = 1 WHERE uploaded = 0")
    void updateHarga();
}
