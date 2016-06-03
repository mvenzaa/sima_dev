package com.venza.stopnarkoba.model;

/**
 * Created by Probook 4341s on 5/19/2016.
 */
public class article {
    int ID;
    String title, image_small_Url;
    String content;
    String created_at;

    public article(){
    }

    public article(int ID, String name, String content, String created_at, String image_small_Url) {
        this.ID = ID;
        this.title = name;
        this.content = content;
        this.created_at = created_at;
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

    public String getImage_small_Url() {
        return image_small_Url;
    }

    public void setImage_small_Url(String image_small_Url) {
        this.image_small_Url = image_small_Url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
