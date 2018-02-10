package com.mahendri.pasbeli.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.mahendri.pasbeli.entity.Pasar;

import java.util.List;

/**
 * @author Mahendri
 */

@Dao
public interface PasarDao {

    @Query("SELECT * FROM Pasar ORDER BY ((latitude - :latUser) * (latitude - :latUser))"
            + " + ((longitude - :lngUser) * (longitude - :lngUser)) ASC"
            + " LIMIT 20")
    LiveData<List<Pasar>> loadDaftarPasar(double latUser, double lngUser);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPasar(Pasar pasar);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDaftarPasar(List<Pasar> daftarPasar);

}
