package com.messages;

import java.io.Serializable;

public class User implements Serializable {
    String name;
    String picture;


    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture(){
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void changePicture(String picture){
        this.picture=picture;
    }
}
