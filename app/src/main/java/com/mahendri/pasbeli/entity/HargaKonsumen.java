package com.mahendri.pasbeli.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * @author Mahendri
 */

@Entity
@SuppressWarnings("unused")
public class HargaKonsumen {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_entry")
    public long idEntry;

    @ColumnInfo(name = "id_barang")
    public int idBarang;

    @ColumnInfo(name = "harga")
    public long harga;

    @ColumnInfo(name = "waktu_catat")
    public long waktuCatat;

    @ColumnInfo(name = "id_tempat")
    public String idTempat;

    @ColumnInfo(name = "nama_tempat")
    public String namaTempat;

    public int uploaded = 0;

    public double latitude;

    public double longitude;

    @Override
    public String toString() {
        return String.format("Nama barang %s, status upload %s", idBarang, uploaded);
    }
}
