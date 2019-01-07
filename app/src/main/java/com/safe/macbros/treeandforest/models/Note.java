package com.safe.macbros.treeandforest.models;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class Note implements Serializable {
    private static final String TAG = "Note";


    private String id;
    private String staffId;
    private String tailgateId;
    private String equipmentId;
    private String vehicleId;
    private String title;
    private String details;
    private Boolean complete;
    private String authorId;
    private Timestamp mTimestamp;
    private String siteId;


    public Note(){


    }


    public Note(String id, String staffId, String tailgateId, String equipmentId, String vehicleId, String title, String details, Boolean complete, String authorId, Timestamp timestamp, String siteId) {
        this.id = id;
        this.staffId = staffId;
        this.tailgateId = tailgateId;
        this.equipmentId = equipmentId;
        this.vehicleId = vehicleId;
        this.title = title;
        this.details = details;
        this.complete = complete;
        this.authorId = authorId;
        mTimestamp = timestamp;
        this.siteId = siteId;
    }


    public static String getTAG() {
        return TAG;
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

    public String getTailgateId() {
        return tailgateId;
    }

    public void setTailgateId(String tailgateId) {
        this.tailgateId = tailgateId;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
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

    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public Timestamp getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        mTimestamp = timestamp;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }
}
