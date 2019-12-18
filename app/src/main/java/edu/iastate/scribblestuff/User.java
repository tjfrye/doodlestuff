package edu.iastate.scribblestuff;

import java.io.Serializable;

public class User implements Serializable {

    public String username;
    public String friendcode;
    public String uid;

    public User() {

    }

    public User(String username, String friendcode, String uid) {
        this.username = username;
        this.friendcode = friendcode;
        this.uid = uid;
    }

    public String getUsername() {
        return this.username;
    }

    public String getFriendcode() {
        return this.friendcode;
    }

    public String getUid() { return this.uid; }
}
