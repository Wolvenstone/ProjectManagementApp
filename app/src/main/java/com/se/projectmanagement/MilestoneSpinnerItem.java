package com.se.projectmanagement;

/**
 * Created by Max on 08.01.2017.
 */

public class MilestoneSpinnerItem {
    private String id;
    private String description;

    public MilestoneSpinnerItem(String id, String description) {
        if (id == null)
            throw new NullPointerException("id must not be NULL");
        this.id = id;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        return description;
    }
}
