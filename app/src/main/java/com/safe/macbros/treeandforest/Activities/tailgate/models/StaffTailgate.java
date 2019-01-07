package com.safe.macbros.treeandforest.Activities.tailgate.models;

import android.util.Log;

import com.google.firebase.firestore.ServerTimestamp;
import com.safe.macbros.treeandforest.models.Equipment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StaffTailgate {
    private static final String TAG = "StaffTailgate";

    private String id;
    private String name;
    private String signUrl;
    private ArrayList<String> Notes;
    private int tally;
    private boolean planPass = false;
    private boolean equipmentPass = false;
    private boolean signPass = false;
    private boolean completed = false;
    private boolean ppeCheckPass =false;
    private boolean plb = false;
    private boolean eqCheckPass = false;

    public StaffTailgate(){}

    public StaffTailgate(String id, String name, String signUrl, ArrayList<String> notes, int tally, boolean planPass, boolean equipmentPass, boolean signPass, boolean completed, boolean ppeCheckPass, boolean plb, boolean eqCheckPass) {
        this.id = id;
        this.name = name;
        this.signUrl = signUrl;
        Notes = notes;
        this.tally = tally;
        this.planPass = planPass;
        this.equipmentPass = equipmentPass;
        this.signPass = signPass;
        this.completed = completed;
        this.ppeCheckPass = ppeCheckPass;
        this.plb = plb;
        this.eqCheckPass = eqCheckPass;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSignUrl() {
        return signUrl;
    }

    public void setSignUrl(String signUrl) {
        this.signUrl = signUrl;
    }

    public ArrayList<String> getNotes() {
        return Notes;
    }

    public void setNotes(ArrayList<String> notes) {
        Notes = notes;
    }

    public int getTally() {
        return tally;
    }

    public void setTally(int tally) {
        this.tally = tally;
    }

    public boolean isPlanPass() {
        return planPass;
    }

    public void setPlanPass(boolean planPass) {
        this.planPass = planPass;
    }

    public boolean isEquipmentPass() {
        return equipmentPass;
    }

    public void setEquipmentPass(boolean equipmentPass) {
        this.equipmentPass = equipmentPass;
    }

    public boolean isSignPass() {
        return signPass;
    }

    public void setSignPass(boolean signPass) {
        this.signPass = signPass;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isPpeCheckPass() {
        return ppeCheckPass;
    }

    public void setPpeCheckPass(boolean ppeCheckPass) {
        this.ppeCheckPass = ppeCheckPass;
    }

    public boolean isPlb() {
        return plb;
    }

    public void setPlb(boolean plb) {
        this.plb = plb;
    }

    public boolean isEqCheckPass() {
        return eqCheckPass;
    }

    public void setEqCheckPass(boolean eqCheckPass) {
        this.eqCheckPass = eqCheckPass;
    }
}
