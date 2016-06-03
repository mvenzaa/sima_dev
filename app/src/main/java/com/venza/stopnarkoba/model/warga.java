package com.venza.stopnarkoba.model;

/**
 * Created by Probook 4341s on 5/21/2016.
 */
public class warga {
    int user_id;
    String name, image_Url;

    public warga(){
    }

    public warga(int user_id, String name, String image_Url) {
        this.user_id = user_id;
        this.name = name;
        this.image_Url = image_Url;

    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_Url() {
        return image_Url;
    }

    public void setImage_Url(String image_Url) {
        this.image_Url = image_Url;
    }
}
