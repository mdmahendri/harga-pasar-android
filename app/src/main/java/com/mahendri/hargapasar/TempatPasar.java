package com.mahendri.hargapasar;

/**
 * @author Mahendri Dwicahyo
 */

public class TempatPasar {

    private int resourceId;
    private String placeTitle;
    private int placeKomoditas;
    private int placeReward;

    public TempatPasar (int resourceId, String placeTitle, int placeKomoditas, int placeReward) {
        this.resourceId = resourceId;
        this.placeTitle = placeTitle;
        this.placeKomoditas = placeKomoditas;
        this.placeReward = placeReward;
    }

    public int getResourceId() {
        return resourceId;
    }

    public String getPlaceTitle() {
        return placeTitle;
    }

    public int getPlaceKomoditas() {
        return placeKomoditas;
    }

    public int getPlaceReward() {
        return placeReward;
    }
}
