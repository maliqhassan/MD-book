package com.alsam.mdbook_01;

import java.io.Serializable;

class Photo implements Serializable {

    private String photoid = "-1";

    private  String filepath;

    public Photo(String path) {
        this.filepath = path;

    }
    public String getPhotoid() {
        return photoid;
    }

    public void setPhotoid(String photoid) {

        this.photoid = photoid;
    }

    public String getFilepath() {

        return filepath;
    }

    public void setFilepath(String path) {

        this.filepath = path;
    }


}

