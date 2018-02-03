package com.mahendri.pasbeli.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.mahendri.pasbeli.entity.data.HargaKomoditas;

/**
 * @author Mahendri
 */

@Database(entities = {HargaKomoditas.class}, version = 1)
public abstract class PasBeliDatabase extends RoomDatabase {

    public abstract HargaDao hargaKomoditasDao();

}
