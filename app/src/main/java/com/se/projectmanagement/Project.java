package com.se.projectmanagement;

/**
 * Created by Roman on 02.01.2017.
 */

public class Project {
    private String id;
    private String title;
    private String description;

    public Project(String id, String title, String description) {
        if (id == null)
            throw new NullPointerException("id must not be NULL");
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        return "Title: " + title + ", ID: " + id + ", Description: " + description;
    }
}
