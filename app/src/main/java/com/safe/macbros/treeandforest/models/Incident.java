package com.safe.macbros.treeandforest.models;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class Incident implements Serializable {

    private String id;
    private String siteId;
    private String staffId;
    private String imageOnlineUrl;
    private String imageOfflineUrl;
    private String siteName;
    private String tailgateId;
    private String gps;
    private String type;
    private String staffName;
    private boolean medical = false;
    private boolean firstAid = false;
    private String vehicleId;
    private String whatHappened;
    private String completedBy;
    private String mainCause;
    private String prevention;
    private boolean completed = false;
    private String title;
    private String followUpIncId;
    Timestamp created;
    Timestamp daysDue;


    public Incident(){}

    public Incident(String id, String siteId, String staffId, String imageOnlineUrl, String imageOfflineUrl, String siteName, String gps, String type,
                    String staffName, boolean medical, boolean firstAid, String vehicleId, String whatHappened, String completedBy, String mainCause,
                    String prevention, boolean completed, String title, String followUpIncId, Timestamp created, Timestamp daysDue) {
        this.id = id;
        this.siteId = siteId;
        this.staffId = staffId;
        this.imageOnlineUrl = imageOnlineUrl;
        this.imageOfflineUrl = imageOfflineUrl;
        this.siteName = siteName;
        this.gps = gps;
        this.type = type;
        this.staffName = staffName;
        this.medical = medical;
        this.firstAid = firstAid;
        this.vehicleId = vehicleId;
        this.whatHappened = whatHappened;
        this.completedBy = completedBy;
        this.mainCause = mainCause;
        this.prevention = prevention;
        this.completed = completed;
        this.title = title;
        this.followUpIncId = followUpIncId;
        this.created = created;
        this.daysDue = daysDue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getImageOnlineUrl() {
        return imageOnlineUrl;
    }

    public void setImageOnlineUrl(String imageOnlineUrl) {
        this.imageOnlineUrl = imageOnlineUrl;
    }

    public String getImageOfflineUrl() {
        return imageOfflineUrl;
    }

    public void setImageOfflineUrl(String imageOfflineUrl) {
        this.imageOfflineUrl = imageOfflineUrl;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public boolean isMedical() {
        return medical;
    }

    public void setMedical(boolean medical) {
        this.medical = medical;
    }

    public boolean isFirstAid() {
        return firstAid;
    }

    public void setFirstAid(boolean firstAid) {
        this.firstAid = firstAid;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getWhatHappened() {
        return whatHappened;
    }

    public void setWhatHappened(String whatHappened) {
        this.whatHappened = whatHappened;
    }

    public String getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(String completedBy) {
        this.completedBy = completedBy;
    }

    public String getMainCause() {
        return mainCause;
    }

    public void setMainCause(String mainCause) {
        this.mainCause = mainCause;
    }

    public String getPrevention() {
        return prevention;
    }

    public void setPrevention(String prevention) {
        this.prevention = prevention;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFollowUpIncId() {
        return followUpIncId;
    }

    public void setFollowUpIncId(String followUpIncId) {
        this.followUpIncId = followUpIncId;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getDaysDue() {
        return daysDue;
    }

    public void setDaysDue(Timestamp daysDue) {
        this.daysDue = daysDue;
    }
}
