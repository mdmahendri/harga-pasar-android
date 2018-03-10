package com.mahendri.pasbeli.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.mahendri.pasbeli.entity.Barang;
import com.mahendri.pasbeli.entity.BarangHarga;
import com.mahendri.pasbeli.entity.HargaKonsumen;

import java.util.List;

/**
 * @author Mahendri
 */

@Dao
public interface HargaDao {

    @Query("SELECT Barang.nama, HargaKonsumen.harga, HargaKonsumen.nama_tempat, HargaKonsumen.waktu_catat" +
            " FROM HargaKonsumen JOIN Barang ON HargaKonsumen.id_barang = Barang.id_barang")
    LiveData<List<BarangHarga>> loadCatatanHarga();

    @Query("SELECT * FROM HargaKonsumen WHERE uploaded = 0")
    List<HargaKonsumen> getUnsendDataHarga();

    @Insert
    long insertHarga(HargaKonsumen harga);

    @Query("UPDATE HargaKonsumen SET uploaded = 1 WHERE uploaded = 0")
    void updateHarga();
}
