package com.mahendri.hargapasar;

/**
 * @author Mahendri Dwicahyo
 */

public class UsulTempat {

    private int placeApprove;
    private int resourceId;
    private String placeTitle;
    private String placeAddress;


    public UsulTempat (int placeApprove, int resourceId, String placeTitle, String placeAddress) {
        this.placeApprove = placeApprove;
        this.resourceId = resourceId;
        this.placeTitle = placeTitle;
        this.placeAddress = placeAddress;
    }

    public int getPlaceApprove() {
        return placeApprove;
    }

    public String getPlaceTitle() {
        return placeTitle;
    }

    public String getPlaceAddress() {
        return placeAddress;
    }

    public int getResourceId() {
        return resourceId;
    }
}
