package com.example.kanj.steallocation.api.pojo;

import com.example.kanj.steallocation.db.Loc;

public class LocationsDetails {
    public double lat;
    public double lng;
    public int accuracy;

    public LocationsDetails(Loc loc) {
        this.lat = loc.lat;
        this.lng = loc.lon;
        this.accuracy = loc.accuracy;
    }
}
