package com.example.hagarhossam.aroundtheblock_version2.model;

/**
 * Created by Hagar Hossam on 19-Mar-16.
 */
public class categorySlideMenu {

    private int imageId;
    private String title;

    public categorySlideMenu( String title) {
        //this.imageId = imageId;
        this.title = title;
    }
/*
    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
*/
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
