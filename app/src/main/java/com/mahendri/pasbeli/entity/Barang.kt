package com.mahendri.pasbeli.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * @author Mahendri
 */

@Entity
data class Barang(@PrimaryKey @ColumnInfo(name = "id_barang") val idBarang: Int,
                  val nama: String,
                  val kualitas: String,
                  val satuan: String)