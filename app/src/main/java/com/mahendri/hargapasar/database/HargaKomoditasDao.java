package com.mahendri.hargapasar.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.mahendri.hargapasar.entity.data.HargaKomoditas;

import java.util.List;

/**
 * @author Mahendri
 */

@Dao
public interface HargaKomoditasDao {

    @Query("SELECT * FROM HargaKomoditas")
    LiveData<List<HargaKomoditas>> loadHargaKomoditasList();

    @Insert
    long insert(HargaKomoditas harga);

}
