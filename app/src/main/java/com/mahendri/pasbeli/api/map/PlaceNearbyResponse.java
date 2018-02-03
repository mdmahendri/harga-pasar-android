package com.mahendri.pasbeli.api.map;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mahendri
 */

public class PlaceNearbyResponse {

    @SerializedName("html_attributions")
    private List<String> htmlAttributions = new ArrayList<>();

    @SerializedName("next_page_token")
    private String nextPageToken;

    private List<PlaceResult> results = new ArrayList<>();
    private String status;

    public  String getStatus() {
        return status;
    }

    public List<PlaceResult> getResults() {
        return results;
    }
}
