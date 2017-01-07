package com.se.projectmanagement;

/**
 * Created by vera on 06.01.2017.
 */

public class Task {
    private String id;
    private String title;
    private String description;
    private String state;
    private String from;
    private String to;
    private String project;
    private String milestone;
    private String user;

    public Task(String id, String title, String description, String state, String from, String to, String project, String milestone, String user) {
        if (id == null)
            throw new NullPointerException("id must not be NULL");
        this.id = id;
        this.title = title;
        this.description = description;
        this.state = state;
        this.from = from;
        this.to = to;
        this.project = project;
        this.milestone = milestone;
        this.user = user;
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

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getState() { return state; }

    public void setState(String state) { this.state = state; }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getMilestone() {
        return milestone;
    }

    public void setMilestone(String milestone) {
        this.milestone = milestone;
    }

    public String getUser() { return user; }

    public void setUser(String user) {
        this.user = user;
    }

    public String toString() {
        return "Title: " + title + "\nID: " + id +  "\nDescription: " + description + "\nState: " + state + "\nUser: " + user
                + "\nProject: " + project + "\nMilestone: " + milestone + "\nFrom: " + from + "\nTo: " + to;
    }
}
