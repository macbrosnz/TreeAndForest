package com.safe.macbros.treeandforest.models;

import java.io.Serializable;

public class Driver implements Serializable {
    private String id;
    private String staffId;
    private String name;
    private int hours = 0;
    private String tailgateId;

    public Driver(){


    }

    public Driver(String id, String staffId, String name, int hours, String tailgateId) {
        this.id = id;
        this.staffId = staffId;
        this.name = name;
        this.hours = hours;
        this.tailgateId = tailgateId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public String getTailgateId() {
        return tailgateId;
    }

    public void setTailgateId(String tailgateId) {
        this.tailgateId = tailgateId;
    }
}
