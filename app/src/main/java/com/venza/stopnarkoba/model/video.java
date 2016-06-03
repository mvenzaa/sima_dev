package com.venza.stopnarkoba.model;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by Probook 4341s on 5/11/2016.
 */
public class video {

    int ID;
    String title, image_small_Url;
    String content;
    int is_streaming;
    String created_at;
    String youtube_id;


    public video(){
    }

    public video(int ID, String name, String content, int is_streaming, String created_at, String youtube_id, String image_small_Url) {
        this.ID = ID;
        this.title = name;
        this.content = content;
        this.is_streaming = is_streaming;
        this.created_at = created_at;
        this.youtube_id = youtube_id;
        this.image_small_Url = image_small_Url;

    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIs_streaming() {
        return is_streaming;
    }

    public void setIs_streaming(int is_streaming) {
        this.is_streaming = is_streaming;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getYoutube_id() {
        return youtube_id;
    }

    public void setYoutube_id(String youtube_id) {
        this.youtube_id = youtube_id;
    }

    public String getImage_small_Url() {
        return image_small_Url;
    }

    public void setImage_small_Url(String image_small_Url) {
        this.image_small_Url = image_small_Url;
    }


}
