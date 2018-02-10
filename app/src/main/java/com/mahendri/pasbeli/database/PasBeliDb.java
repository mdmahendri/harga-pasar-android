package com.mahendri.pasbeli.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.mahendri.pasbeli.entity.HargaKomoditas;
import com.mahendri.pasbeli.entity.Pasar;

/**
 * @author Mahendri
 */

@Database(entities = {HargaKomoditas.class, Pasar.class}, version = 1)
public abstract class PasBeliDb extends RoomDatabase {

    public abstract HargaDao hargaKomoditasDao();
    public abstract PasarDao pasarDao();

}
