package com.mahendri.pasbeli.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.mahendri.pasbeli.entity.Barang;
import com.mahendri.pasbeli.entity.HargaKonsumen;
import com.mahendri.pasbeli.entity.Pasar;

/**
 * @author Mahendri
 */

@Database(entities = {HargaKonsumen.class, Pasar.class, Barang.class}, version = 1, exportSchema = false)
public abstract class PasBeliDb extends RoomDatabase {

    public abstract HargaDao hargaKomoditasDao();
    public abstract PasarDao pasarDao();
    public abstract BarangDao barangDao();

}
