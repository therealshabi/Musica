package com.finiteloop.musica.Models;

/**
 * Created by Shivam on 20-04-2017.
 */

public class PostModel {

    String genreTag;
    String title;
    String post_pic_url;
    String no_of_likes;
    String no_of_loves;

    // getter methods
    public String getGenreTag() {
        return genreTag;
    }
    public String getTitle() {
        return title;
    }
    public String getPost_pic_url() {
        return post_pic_url;
    }
    public String getNo_of_likes() {
        return no_of_likes;
    }
    public String getNo_of_loves() {
        return no_of_loves;
    }

    // setter methods
    public void setTitle(String description) {
        this.title = title;
    }
    public void setGenreTag(String genreTag) {
        this.genreTag = genreTag;
    }
    public void setNo_of_likes(String no_of_likes) {
        this.no_of_likes = no_of_likes;
    }
    public void setNo_of_loves(String no_of_loves) {
        this.no_of_loves = no_of_loves;
    }
    public void setPost_pic_url(String post_pic_url) {
        this.post_pic_url = post_pic_url;
    }
}
