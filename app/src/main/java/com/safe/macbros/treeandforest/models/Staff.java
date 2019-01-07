package com.safe.macbros.treeandforest.models;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;

@IgnoreExtraProperties
public class Staff implements Serializable {
    private static final String TAG = "Staff";
    private String id;
    private String name;
    private String mobile;
    private String onlineImageUrl;
    private String offlineImageUrl;
    private boolean firstaid = false;
    private boolean fireCert = false;
    private String licence;
    private ArrayList<String> qualifications = new ArrayList<>();

    public Staff(){

    }

    public Staff(String id, String name, String mobile, String onlineImageUrl, String offlineImageUrl, boolean firstaid, boolean fireCert, String licence, ArrayList<String> qualifications) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.onlineImageUrl = onlineImageUrl;
        this.offlineImageUrl = offlineImageUrl;
        this.firstaid = firstaid;
        this.fireCert = fireCert;
        this.licence = licence;
        this.qualifications = qualifications;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOnlineImageUrl() {
        return onlineImageUrl;
    }

    public void setOnlineImageUrl(String onlineImageUrl) {
        this.onlineImageUrl = onlineImageUrl;
    }

    public String getOfflineImageUrl() {
        return offlineImageUrl;
    }

    public void setOfflineImageUrl(String offlineImageUrl) {
        this.offlineImageUrl = offlineImageUrl;
    }

    public boolean isFirstaid() {
        return firstaid;
    }

    public void setFirstaid(boolean firstaid) {
        this.firstaid = firstaid;
    }

    public boolean isFireCert() {
        return fireCert;
    }

    public void setFireCert(boolean fireCert) {
        this.fireCert = fireCert;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public ArrayList<String> getQualifications() {
        return qualifications;
    }

    public void setQualifications(ArrayList<String> qualifications) {
        this.qualifications = qualifications;
    }
}
