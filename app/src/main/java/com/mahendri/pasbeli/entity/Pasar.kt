package com.mahendri.pasbeli.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull

import com.google.android.gms.location.places.Place
import com.google.android.gms.maps.model.LatLng

/**
 * @author Mahendri
 */

@Entity
data class Pasar(
        @NonNull @PrimaryKey
        var id: String,
        var nama: String,
        var alamat: String,
        var latitude: Double,
        var longitude: Double,
        var version: Int = 0
) {

    val location: LatLng
        get() = LatLng(latitude, longitude)

    constructor(place: Place) : this(
            place.id, place.name.toString(),
            place.address.toString(),
            place.latLng.latitude,
            place.latLng.longitude
    )
}
