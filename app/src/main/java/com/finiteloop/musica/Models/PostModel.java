package com.finiteloop.musica.Models;

import java.sql.Timestamp;
import java.util.Comparator;

/**
 * Created by Shivam on 20-04-2017.
 */

public class PostModel {

    public static final Comparator<PostModel> hitsComparator = new MyComparator();
    public static final Comparator<PostModel> timeStampComparator = new newTimeComparator();
    String post_id;
    String genreTag;
    String title;
    String post_pic_url;
    String no_of_likes;
    String no_of_loves;
    String user_profile_pic;
    String username;
    Timestamp timeStamp;
    String postURL;
    boolean privatePost;
    int hits;

    public boolean isPrivatePost() {
        return privatePost;
    }

    public void setPrivatePost(boolean privatePost) {
        this.privatePost = privatePost;
    }

    public String getPostURL() {
        return postURL;
    }

    public void setPostURL(String postURL) {
        this.postURL = postURL;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
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

    static class MyComparator implements Comparator<PostModel> {

        @Override
        public int compare(PostModel o1, PostModel o2) {
            if (o1.getHits() > o2.getHits())
                return -1;
            if (o1.getHits() < o2.getHits())
                return 1;
            return 0;
        }
    }

    private static class newTimeComparator implements Comparator<PostModel> {
        @Override
        public int compare(PostModel o1, PostModel o2) {
            return o1.getTimeStamp().compareTo(o2.getTimeStamp());
        }
    }
}
