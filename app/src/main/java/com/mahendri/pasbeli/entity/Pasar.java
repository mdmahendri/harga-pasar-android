package com.mahendri.pasbeli.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * @author Mahendri
 */

@Entity
public class Pasar {

    @NonNull
    @PrimaryKey
    public String uuid;

    public String nama;

    public String alamat;

    public double latitude;

    public double longitude;

}
