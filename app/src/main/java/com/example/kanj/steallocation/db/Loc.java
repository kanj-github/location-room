package com.example.kanj.steallocation.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Loc {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public Double lat;
    public Double lon;
    public int accuracy;

    public Loc (Double lat, Double lon, int accuracy) {
        this.lat = lat;
        this.lon = lon;
        this.accuracy = accuracy;
    }
}
