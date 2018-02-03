package com.mahendri.pasbeli.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * @author Mahendri
 */

@Entity
@SuppressWarnings("unused")
public class HargaKomoditas {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_entry")
    public long idEntry;

    @ColumnInfo(name = "nama_komoditas")
    public String namaKomoditas;

    @ColumnInfo(name = "harga_komoditas")
    public long hargaKomoditas;

    @ColumnInfo(name = "waktu_catat")
    public long waktuCatat;

    @ColumnInfo(name = "id_tempat")
    public String idTempat;

    @ColumnInfo(name = "nama_tempat")
    public String namaTempat;

    public double latitude;

    public double longitude;
}
