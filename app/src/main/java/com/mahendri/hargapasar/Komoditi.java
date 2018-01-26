package com.mahendri.hargapasar;

/**
 * @author Mahendri Dwicahyo
 */

public class Komoditi {

    private int resourceId;
    private int komoditiReward;
    private int minPrice;
    private int maxPrice;
    private String komoditiName;
    private String komoditiSatuan;

    public Komoditi (int resourceId, int komoditiReward, int minPrice, int maxPrice, String komoditiName,
                     String komoditiSatuan) {
        this.resourceId = resourceId;
        this.komoditiReward = komoditiReward;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.komoditiName = komoditiName;
        this.komoditiSatuan = komoditiSatuan;
    }

    public int getResourceId() {
        return resourceId;
    }

    public int getKomoditiReward() {
        return komoditiReward;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public String getKomoditiName() {
        return komoditiName;
    }

    public String getKomoditiSatuan() {
        return komoditiSatuan;
    }
}
