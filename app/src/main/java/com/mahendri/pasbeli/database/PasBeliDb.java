package com.mahendri.pasbeli.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.support.annotation.NonNull;

import com.mahendri.pasbeli.entity.Barang;
import com.mahendri.pasbeli.entity.HargaKonsumen;
import com.mahendri.pasbeli.entity.Pasar;

import javax.inject.Singleton;

/**
 * @author Mahendri
 */

@Singleton
@Database(entities = {HargaKonsumen.class, Pasar.class, Barang.class}, version = 2, exportSchema = false)
public abstract class PasBeliDb extends RoomDatabase {

    public abstract HargaDao hargaKomoditasDao();
    public abstract PasarDao pasarDao();
    public abstract BarangDao barangDao();

    /**
     * Migrate from version 1 to 2
     * Pasar table has a new field: version
     */
    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Pasar ADD COLUMN version INTEGER");
            database.execSQL("UPDATE Pasar SET version = 1");
        }
    };
}
