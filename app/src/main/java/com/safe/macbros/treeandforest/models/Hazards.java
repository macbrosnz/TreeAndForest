package com.safe.macbros.treeandforest.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

@IgnoreExtraProperties
public class Hazards implements Serializable {

    private String id;
    private String equipmentId;
    private ArrayList<String> taskId = new ArrayList<>();
    private int riskScore = 0;
    private String details;
    private String title;
    private String control;
    private String eim;
    private String onlinePath;
    private String offlinePath;
    private Timestamp created;
    private Timestamp lastReviewed;


    public Hazards(){

    }

    public Hazards(String id, String equipmentId, ArrayList<String> taskId, int riskScore,
                   String details, String title, String control, String eim, String onlinePath,
                   String offlinePath, Timestamp created, Timestamp lastReviewed) {
        this.id = id;
        this.equipmentId = equipmentId;
        this.taskId = taskId;
        this.riskScore = riskScore;
        this.details = details;
        this.title = title;
        this.control = control;
        this.eim = eim;
        this.onlinePath = onlinePath;
        this.offlinePath = offlinePath;
        this.created = created;
        this.lastReviewed = lastReviewed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public ArrayList<String> getTaskId() {
        return taskId;
    }

    public void setTaskId(ArrayList<String> taskId) {
        this.taskId = taskId;
    }

    public int getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(int riskScore) {
        this.riskScore = riskScore;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control;
    }

    public String getEim() {
        return eim;
    }

    public void setEim(String eim) {
        this.eim = eim;
    }

    public String getOnlinePath() {
        return onlinePath;
    }

    public void setOnlinePath(String onlinePath) {
        this.onlinePath = onlinePath;
    }

    public String getOfflinePath() {
        return offlinePath;
    }

    public void setOfflinePath(String offlinePath) {
        this.offlinePath = offlinePath;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getLastReviewed() {
        return lastReviewed;
    }

    public void setLastReviewed(Timestamp lastReviewed) {
        this.lastReviewed = lastReviewed;
    }
}
