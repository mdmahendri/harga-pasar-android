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
            database.execSQL("ALTER TABLE Pasar RENAME TO Pasar_old");
            database.execSQL("CREATE TABLE Pasar (id TEXT PRIMARY KEY NOT NULL, nama TEXT, alamat TEXT," +
                    "latitude REAL NOT NULL, longitude REAL NOT NULL, version INTEGER NOT NULL DEFAULT 0)");
            database.execSQL("INSERT INTO Pasar (id, nama, alamat, latitude, longitude, version)" +
                    " SELECT uuid, nama, alamat, latitude, longitude, 1 FROM Pasar_old");
            database.execSQL("DROP TABLE Pasar_old");
        }
    };
}
