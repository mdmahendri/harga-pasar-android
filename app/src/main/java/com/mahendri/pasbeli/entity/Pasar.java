package com.mahendri.pasbeli.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

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

    public LatLng getLocation() {
        return new LatLng(latitude, longitude);
    }
}
