package com.example.user.artfolio_ver10;

/**
 * Created by user on 2017-12-03.
 */

public class userlist_item {
    private String image_url;
    private String user_name;
    private int fa_totalnum;
    public userlist_item(String image_url, String user_name, int fa_totalnum) {
        this.image_url = image_url;
        this.user_name = user_name;
        this.fa_totalnum = fa_totalnum;
    }

    public int getFa_totalnum() {
        return fa_totalnum;
    }

    public void setFa_totalnum(int fa_totalnum) {
        this.fa_totalnum = fa_totalnum;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
