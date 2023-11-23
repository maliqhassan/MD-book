package com.alsam.mdbook_01;

import java.io.Serializable;

class GeoLocation implements Serializable {
    private Double Lat;
    private Double Long;
    private String title;

    public GeoLocation(Double Lat, Double Long, String title){
        this.Lat = Lat;
        this.Long = Long;
        this.title = title;
    }

    public Double getLat() {
        return Lat;
    }

    public Double getLong() {
        return Long;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLat(Double lat) {
        Lat = lat;
    }

    public void setLong(Double aLong) {
        Long = aLong;
    }
}

