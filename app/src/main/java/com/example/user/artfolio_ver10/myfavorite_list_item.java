package com.example.user.artfolio_ver10;

/**
 * Created by user on 2017-12-07.
 */

public class myfavorite_list_item {
    private String profile_url;
    private String user_name;
    public myfavorite_list_item(String profile_url, String user_name){
        this.profile_url = profile_url;
        this.user_name = user_name;
    }
    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
