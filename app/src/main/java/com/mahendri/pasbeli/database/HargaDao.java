package com.mahendri.pasbeli.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.mahendri.pasbeli.entity.data.HargaKomoditas;

import java.util.List;

/**
 * @author Mahendri
 */

@Dao
public interface HargaDao {

    @Query("SELECT * FROM HargaKomoditas")
    LiveData<List<HargaKomoditas>> loadHargaKomoditasList();

    @Insert
    long insert(HargaKomoditas harga);

}
