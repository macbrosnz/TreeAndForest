package com.safe.macbros.treeandforest.Activities.tailgate.models;

import java.io.Serializable;
import java.util.Date;

public class EquipmentTailgate implements Serializable {

    private String id;
    private String tailgateId;
    private String staffId;
    private String name;
    private String note;
    private String alertId;
    private String noteId;
    private String maintenance;
    private String standards;
    private Boolean pass;
    private Boolean ppe;
    private Date alertDate;

    public EquipmentTailgate(){

    }


    public EquipmentTailgate(String id, String tailgateId, String staffId, String name,
                             String note, String alertId, String noteId, String maintenance,
                             String standards, Boolean pass, Boolean ppe, Date alertDate) {
        this.id = id;
        this.tailgateId = tailgateId;
        this.staffId = staffId;
        this.name = name;
        this.note = note;
        this.alertId = alertId;
        this.noteId = noteId;
        this.maintenance = maintenance;
        this.standards = standards;
        this.pass = pass;
        this.ppe = ppe;
        this.alertDate = alertDate;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAlertId() {
        return alertId;
    }

    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(String maintenance) {
        this.maintenance = maintenance;
    }

    public String getStandards() {
        return standards;
    }

    public void setStandards(String standards) {
        this.standards = standards;
    }

    public Boolean getPass() {
        return pass;
    }

    public void setPass(Boolean pass) {
        this.pass = pass;
    }

    public Boolean getPpe() {
        return ppe;
    }

    public void setPpe(Boolean ppe) {
        this.ppe = ppe;
    }

    public Date getAlertDate() {
        return alertDate;
    }

    public void setAlertDate(Date alertDate) {
        this.alertDate = alertDate;
    }
}
