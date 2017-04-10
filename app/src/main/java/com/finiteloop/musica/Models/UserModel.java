package com.finiteloop.musica.Models;

/**
 * Created by therealshabi on 10/04/17.
 */

public class UserModel {
    String email;
    String userId;
    String username;

    public UserModel(String username, String email) {
        this.email = email;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserid(String userid) {
        this.userId = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
