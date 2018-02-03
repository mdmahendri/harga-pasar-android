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

    @Query("SELECT * FROM Pasar")
    LiveData<List<Pasar>> loadDaftarPasar();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPasar(List<Pasar> daftarPasar);

}
