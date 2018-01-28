package com.mahendri.hargapasar.entity.map;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * @author Mahendri
 */

public class PlaceLocation {

    private double lat;
    private double lng;

    LatLng getLocation() {
        return new LatLng(lat, lng);
    }
}
