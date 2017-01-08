package com.se.projectmanagement;

/**
 * Created by Max on 07.01.2017.
 */

public class User {
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private String facebookId;

    public User(String id, String firstname, String lastname, String email, String facebookId) {
        if (id == null)
            throw new NullPointerException("id must not be NULL");
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.facebookId = facebookId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String toString() {
        return "ID: " + id + "\nFirstname: " + firstname + "\nLastname: " + lastname + "\nEmail: " + email +
                "\nFacebook-ID: " + facebookId;
    }
}
