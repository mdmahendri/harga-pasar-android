package com.mahendri.hargapasar.entity.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * @author Mahendri
 */

@Entity
@SuppressWarnings("unused")
public class HargaKomoditas {

    @PrimaryKey
    @ColumnInfo(name = "id_entry")
    private long idEntry;

    @ColumnInfo(name = "nama_komoditas")
    private String namaKomoditas;

    @ColumnInfo(name = "harga_komoditas")
    private long hargaKomoditas;

    @ColumnInfo(name = "waktu_catat")
    private long waktuCatat;

    @ColumnInfo(name = "id_tempat")
    private String idTempat;

    private double latitude;

    private double longitude;

    public long getIdEntry() {
        return idEntry;
    }

    public void setIdEntry(long idEntry) {
        this.idEntry = idEntry;
    }

    public String getNamaKomoditas() {
        return namaKomoditas;
    }

    public void setNamaKomoditas(String namaKomoditas) {
        this.namaKomoditas = namaKomoditas;
    }

    public long getHargaKomoditas() {
        return hargaKomoditas;
    }

    public void setHargaKomoditas(long hargaKomoditas) {
        this.hargaKomoditas = hargaKomoditas;
    }

    public long getWaktuCatat() {
        return waktuCatat;
    }

    public void setWaktuCatat(long waktuCatat) {
        this.waktuCatat = waktuCatat;
    }

    public String getIdTempat() {
        return idTempat;
    }

    public void setIdTempat(String idTempat) {
        this.idTempat = idTempat;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
