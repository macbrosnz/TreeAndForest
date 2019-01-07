package com.safe.macbros.treeandforest.models;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class Tallies implements Serializable {

    private String id;
    private String tailgateId;
    private String tailgateName;
    private String staffId;
    private String taskId;
    private String taskName;
    private String tally;
    private Timestamp created = Timestamp.now();

    public Tallies(){


    }

    public Tallies(String id, String tailgateId, String tailgateName, String staffId,
                   String taskId, String taskName, String tally, Timestamp created) {
        this.id = id;
        this.tailgateId = tailgateId;
        this.tailgateName = tailgateName;
        this.staffId = staffId;
        this.taskId = taskId;
        this.taskName = taskName;
        this.tally = tally;
        this.created = created;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTailgateId() {
        return tailgateId;
    }

    public void setTailgateId(String tailgateId) {
        this.tailgateId = tailgateId;
    }

    public String getTailgateName() {
        return tailgateName;
    }

    public void setTailgateName(String tailgateName) {
        this.tailgateName = tailgateName;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTally() {
        return tally;
    }

    public void setTally(String tally) {
        this.tally = tally;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }
}
