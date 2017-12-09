package com.example.user.artfolio_ver10;

/**
 * Created by user on 2017-11-27.
 */

public class vidlist_item
{
    private String video_url;
    private String video_name;

    public vidlist_item(String video_url, String video_name) {
        this.video_url = video_url;
        this.video_name = video_name;
    }

    public String getVideo_name() {
        return video_name;
    }

    public void setVideo_name(String video_name) {
        this.video_name = video_name;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }
}
