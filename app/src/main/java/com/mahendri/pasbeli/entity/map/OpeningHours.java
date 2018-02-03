package com.mahendri.pasbeli.entity.map;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mahendri
 */

public class OpeningHours {

    @SerializedName("open_now")
    private Boolean openNow;

    @SerializedName("weekday_text")
    private List<Object> weekdayText = new ArrayList<>();
}
