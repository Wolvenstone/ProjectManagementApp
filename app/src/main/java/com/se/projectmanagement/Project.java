package com.se.projectmanagement;

/**
 * Created by Roman on 02.01.2017.
 */

public class Project {
    private String name;
    private String description;

    public Project(String name, String description) {
        if (name == null)
            throw new NullPointerException("name must not be NULL");
        this.name = name;
        this.description = description;
    }
}
