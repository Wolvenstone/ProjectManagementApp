package com.se.projectmanagement;

/**
 * Created by Roman on 08.01.2017.
 */

public class Solution {
    private String id;
    private String task;
    private String user;
    private String text;
    private String problem;

    public Solution (String id, String task, String user, String text, String problem) {
        if (id == null)
            throw new NullPointerException("id must not be NULL");
        this.id = id;
        //this.title = title;
        this.task = task;
        this.user= user;
        this.text = text;
        this.problem = problem;
    }
    public String getId() {
        return id;
    }
    public String getUser() {
        return user;
    }
    public String getText() {
        return text;
    }
    public String toString() {
        return text;
    }
}
