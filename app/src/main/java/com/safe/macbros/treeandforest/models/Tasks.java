package com.safe.macbros.treeandforest.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class Tasks {
    private static final String TAG = "Tasks";

    //vars
    private String id;
    private String name;
    private String onlineImagePath;
    private String offlineImagePath;
    private int hits;
    private String description;
    private List<String> qualsList;
    private boolean tallies = false;

    public Tasks(){

    }

    public Tasks(String id, String name, String onlineImagePath, String offlineImagePath, int hits, String description, List<String> qualsList, boolean tallies) {
        this.id = id;
        this.name = name;
        this.onlineImagePath = onlineImagePath;
        this.offlineImagePath = offlineImagePath;
        this.hits = hits;
        this.description = description;
        this.qualsList = qualsList;
        this.tallies = tallies;
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

    public String getOnlineImagePath() {
        return onlineImagePath;
    }

    public void setOnlineImagePath(String onlineImagePath) {
        this.onlineImagePath = onlineImagePath;
    }

    public String getOfflineImagePath() {
        return offlineImagePath;
    }

    public void setOfflineImagePath(String offlineImagePath) {
        this.offlineImagePath = offlineImagePath;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getQualsList() {
        return qualsList;
    }

    public void setQualsList(List<String> qualsList) {
        this.qualsList = qualsList;
    }

    public boolean isTallies() {
        return tallies;
    }

    public void setTallies(boolean tallies) {
        this.tallies = tallies;
    }
}