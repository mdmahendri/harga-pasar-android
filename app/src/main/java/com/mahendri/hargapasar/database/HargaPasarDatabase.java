package com.mahendri.hargapasar.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.mahendri.hargapasar.entity.data.HargaKomoditas;

/**
 * @author Mahendri
 */

@Database(entities = {HargaKomoditas.class}, version = 1)
public abstract class HargaPasarDatabase extends RoomDatabase {

    public abstract HargaKomoditasDao hargaKomoditasDao();

}
