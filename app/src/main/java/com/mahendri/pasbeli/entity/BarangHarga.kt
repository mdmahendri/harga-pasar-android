package com.mahendri.pasbeli.entity

import android.arch.persistence.room.ColumnInfo
import com.mahendri.pasbeli.util.AppUtil

/**
 * @author Mahendri
 */
data class BarangHarga(val nama: String,
                       val harga: Long,
                       @ColumnInfo(name = "nama_tempat") val namaTempat: String,
                       @ColumnInfo(name = "waktu_catat") val waktuCatat: Long) {

    val strWaktu: String get() = AppUtil.convertStringDate(waktuCatat)

}