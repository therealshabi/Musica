package com.finiteloop.musica.Models;

import java.util.ArrayList;

/**
 * Created by therealshabi on 10/04/17.
 */

public class UserModel {
    String id;
    String email;
    String username;
    String password;
    String userInfo;
    String profilePicUrl;
    ArrayList<String> followerList;
    ArrayList<String> followingList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public ArrayList<String> getFollowerList() {
        return followerList;
    }

    public void setFollowerList(ArrayList<String> followerList) {
        this.followerList = followerList;
    }

    public ArrayList<String> getFollowingList() {
        return followingList;
    }

    public void setFollowingList(ArrayList<String> followingList) {
        this.followingList = followingList;
    }
}
