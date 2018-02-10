package com.mahendri.pasbeli.util;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.mahendri.pasbeli.api.map.PlaceResult;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @author Mahendri
 */

public class DistanceConvert {

    public static String toKm(Location currentLocation, LatLng latLng) {

        // konversi LatLng ke Location
        Location loc = new Location("");
        loc.setLatitude(latLng.latitude);
        loc.setLongitude(latLng.longitude);
        float distance = currentLocation.distanceTo(loc) / 1000;

        // pembulatan distance
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        decimalFormat.setRoundingMode(RoundingMode.CEILING);
        return decimalFormat.format(distance);
    }
}
