/**
 * Created by vera on 04.01.2017.
 */
package com.se.projectmanagement;
public class Milestones {

    private String name;
    private String description;
    private String project;

    public Milestones(String name, String description) {
        if (name == null)
            throw new NullPointerException("name must not be NULL");
        this.name = name;
        this.description = description;
        this.project = project;
    }
}
