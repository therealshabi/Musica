package com.finiteloop.musica.Models;

/**
 * Created by Shivam on 20-04-2017.
 */

public class PostModel {

    String post_id;
    String genreTag;
    String title;
    String post_pic_url;
    String no_of_likes;
    String no_of_loves;
    String user_profile_pic;
    String username;
    String timeStamp;

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_profile_pic() {
        return user_profile_pic;
    }

    public void setUser_profile_pic(String user_profile_pic) {
        this.user_profile_pic = user_profile_pic;
    }
    // getter methods

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getGenreTag() {
        return genreTag;
    }

    public void setGenreTag(String genreTag) {
        this.genreTag = genreTag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // setter methods

    public String getPost_pic_url() {
        return post_pic_url;
    }

    public void setPost_pic_url(String post_pic_url) {
        this.post_pic_url = post_pic_url;
    }

    public String getNo_of_likes() {
        return no_of_likes;
    }

    public void setNo_of_likes(String no_of_likes) {
        this.no_of_likes = no_of_likes;
    }

    public String getNo_of_loves() {
        return no_of_loves;
    }

    public void setNo_of_loves(String no_of_loves) {
        this.no_of_loves = no_of_loves;
    }
}
