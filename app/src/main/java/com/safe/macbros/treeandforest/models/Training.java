package com.safe.macbros.treeandforest.models;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class Training implements Serializable {

    private String id;
    private String staffId;
    private String checkerId;
    private String qualId;
    private String title;
    private String details;
    private boolean completed = false;
    private Timestamp created;

    public Training(){

    }

    public Training(String id, String staffId, String checkerId, String qualId, String title, String details, boolean completed, Timestamp created) {
        this.id = id;
        this.staffId = staffId;
        this.checkerId = checkerId;
        this.qualId = qualId;
        this.title = title;
        this.details = details;
        this.completed = completed;
        this.created = created;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getCheckerId() {
        return checkerId;
    }

    public void setCheckerId(String checkerId) {
        this.checkerId = checkerId;
    }

    public String getQualId() {
        return qualId;
    }

    public void setQualId(String qualId) {
        this.qualId = qualId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean complete) {
        this.completed = complete;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }
}
