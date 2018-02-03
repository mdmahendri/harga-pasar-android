package com.mahendri.pasbeli.entity.map;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mahendri
 */

public class PlacePhoto {

    @SerializedName("html_attributions")
    private List<String> htmlAttributions = new ArrayList<String>();

    @SerializedName("photo_reference")
    private String photoReference;

    private int height;
    private int width;

}
