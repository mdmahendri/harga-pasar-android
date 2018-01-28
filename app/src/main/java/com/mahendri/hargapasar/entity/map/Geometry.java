package com.mahendri.hargapasar.entity.map;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * @author Mahendri
 */

public class Geometry {

    private PlaceLocation location;

    LatLng getLocation() {
        return location.getLocation();
    }
}
