package com.alsam.mdbook_01;

import java.io.Serializable;

class BodyLocation implements Serializable {

    private  int Frontx;
    private int Fronty;
    private  int Backx;
    private int Backy;

    private Photo photo;

    public BodyLocation() {
        Frontx = 0;
        Fronty = 0;
        Backx = 0;
        Backy = 0;

    }

    //public void uploadPhoto(){}

    public void setFrontLoc(int x, int y){
        this.Frontx = x;
        this.Fronty = y;
    }

    public void setBackLoc(int x, int y){
        this.Backx = x;
        this.Backy = y;
    }

    public int getFrontx(){
        return this.Frontx;
    }
    public int getFronty(){
        return this.Fronty;
    }
    public int getBackx(){
        return this.Backx;
    }
    public int getBacky(){
        return this.Backy;
    }

//    public void setLocation(String location){
//        this.location = location;
//    }
//    public String getLocation(){
//        return this.location;
//    }

    public void setPhoto(Photo photo){
        this.photo = photo;
    }
    public Photo getPhoto(){
        return photo;
    }
    public void deletePhoto(Photo photo){this.photo = null;}

}
