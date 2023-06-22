package com.mad_lab.a1_loginpage.inventory;

import java.io.Serializable;

public class Priority implements Serializable {

    private String name;
    private int image;

    public Priority(){

    }

    public Priority(String name, int image){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
