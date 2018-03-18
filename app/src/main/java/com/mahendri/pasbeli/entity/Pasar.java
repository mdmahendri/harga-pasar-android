package com.mahendri.pasbeli.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

/**
 * @author Mahendri
 */

@Entity
public class Pasar {

    @NonNull
    @PrimaryKey
    public String id;

    public String nama;

    public String alamat;

    public double latitude;

    public double longitude;

    public Pasar() {}

    public Pasar(Place place) {
        this.id = place.getId();
        this.nama = place.getName().toString();
        this.alamat = place.getAddress().toString();
        this.latitude = place.getLatLng().latitude;
        this.longitude = place.getLatLng().longitude;
    }

    public LatLng getLocation() {
        return new LatLng(latitude, longitude);
    }
}
