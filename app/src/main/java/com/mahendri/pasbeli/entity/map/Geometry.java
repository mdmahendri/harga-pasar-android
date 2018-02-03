package com.mahendri.pasbeli.entity.map;

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
