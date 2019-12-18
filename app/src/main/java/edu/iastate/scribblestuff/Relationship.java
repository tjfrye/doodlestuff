package edu.iastate.scribblestuff;

import java.io.Serializable;

public class Relationship implements Serializable {

    public String user1;
    public String user2;
    public int status;

    public Relationship() {

    }

    public Relationship(String user1, String user2, int status) {
        this.user1 = user1;
        this.user2 = user2;
        this.status = status;
    }

    public String getUser1() { return this.user1; }

    public String getUser2() { return this.user2; }

    public int getStatus() { return this.status; }
}
