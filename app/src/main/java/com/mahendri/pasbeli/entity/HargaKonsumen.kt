package com.mahendri.pasbeli.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * @author Mahendri
 */

@Entity
data class HargaKonsumen (
        @ColumnInfo(name = "id_barang")
        var idBarang: Int,
        @ColumnInfo(name = "harga")
        var harga: Long,
        @ColumnInfo(name = "waktu_catat")
        var waktuCatat: Long,
        @ColumnInfo(name = "nama_tempat")
        var namaTempat: String?,
        var mail: String?,
        var latitude: Double,
        var longitude: Double
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_entry")
    var idEntry: Long = 0

    @ColumnInfo(name = "id_tempat")
    var idTempat: String? = null

    var uploaded: Int = 0
}