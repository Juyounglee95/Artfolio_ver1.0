package com.example.user.artfolio_ver10;

/**
 * Created by user on 2017-11-27.
 */

public class piclist_item
{
    private String image_url;
    private String image_name;

    public piclist_item(String image_url, String image_name) {
        this.image_url = image_url;
        this.image_name = image_name;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
