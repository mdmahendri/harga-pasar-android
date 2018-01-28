package com.mahendri.hargapasar.entity.map;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mahendri
 */

public class PlaceResult {

    @SerializedName("opening_hours")
    private OpeningHours openingHours;

    @SerializedName("place_id")
    private String placeId;

    @SerializedName("price_level")
    private Integer priceLevel;

    private double rating;
    private String reference;
    private String scope;
    private Geometry geometry;
    private String icon;
    private String id;
    private String name;
    private String vicinity;
    private List<PlacePhoto> photos = new ArrayList<>();
    private List<String> types = new ArrayList<>();

    public LatLng getLocation() {
        return geometry.getLocation();
    }

    public String getName() {
        return name;
    }
}
