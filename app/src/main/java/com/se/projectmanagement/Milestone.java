/**
 * Created by vera on 04.01.2017.
 */
package com.se.projectmanagement;

public class Milestone {
    private String id;
    private String to;
    private String description;

    public Milestone(String id, String to, String description) {
        if (id == null)
            throw new NullPointerException("id must not be NULL");
        this.id = id;
        this.to = to;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        return  "Description: " + description + "\nID: " + id +  "\nTo: " + to;
    }
}