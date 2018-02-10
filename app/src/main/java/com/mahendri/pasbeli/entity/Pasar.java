package com.mahendri.pasbeli.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.mahendri.pasbeli.api.map.PlaceResult;

/**
 * @author Mahendri
 */

@Entity
public class Pasar {

    @NonNull
    @PrimaryKey
    public String uuid;

    public String nama;

    public String alamat;

    public double latitude;

    public double longitude;

    public Pasar(PlaceResult placeResult) {
        uuid = placeResult.getPlaceId();
        nama = placeResult.getName();
        alamat = placeResult.getVicinity();
        LatLng latLng = placeResult.getLocation();
        latitude = latLng.latitude;
        longitude = latLng.longitude;
    }

    public LatLng getLocation() {
        return new LatLng(latitude, longitude);
    }
}
