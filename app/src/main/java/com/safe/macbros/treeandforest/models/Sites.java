package com.safe.macbros.treeandforest.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class Sites implements Parcelable {
    private static final String TAG = "Sites";

    private String id;
    private String details;
    private String onlinePdfPath;
    private String offlinePdfPath;
    private String name;
    private String target;
    private String area;
    private String access;
    private String radio;
    private String gps;
    private String forest;
    private int mandays;
    private Timestamp created;
    private ArrayList<String> hazards;

    public Sites(){

    }

    public Sites(String id, String details, String onlinePdfPath, String offlinePdfPath, String name, String target, String area, String access, String radio, String gps, String forest, int mandays, Timestamp created, ArrayList<String> hazards) {
        this.id = id;
        this.details = details;
        this.onlinePdfPath = onlinePdfPath;
        this.offlinePdfPath = offlinePdfPath;
        this.name = name;
        this.target = target;
        this.area = area;
        this.access = access;
        this.radio = radio;
        this.gps = gps;
        this.forest = forest;
        this.mandays = mandays;
        this.created = created;
        this.hazards = hazards;
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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getOnlinePdfPath() {
        return onlinePdfPath;
    }

    public void setOnlinePdfPath(String onlinePdfPath) {
        this.onlinePdfPath = onlinePdfPath;
    }

    public String getOfflinePdfPath() {
        return offlinePdfPath;
    }

    public void setOfflinePdfPath(String offlinePdfPath) {
        this.offlinePdfPath = offlinePdfPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getRadio() {
        return radio;
    }

    public void setRadio(String radio) {
        this.radio = radio;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getForest() {
        return forest;
    }

    public void setForest(String forest) {
        this.forest = forest;
    }

    public int getMandays() {
        return mandays;
    }

    public void setMandays(int mandays) {
        this.mandays = mandays;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public ArrayList<String> getHazards() {
        return hazards;
    }

    public void setHazards(ArrayList<String> hazards) {
        this.hazards = hazards;
    }

    protected Sites(Parcel in) {
        id = in.readString();
        details = in.readString();
        onlinePdfPath = in.readString();
        offlinePdfPath = in.readString();
        name = in.readString();
        target = in.readString();
        area = in.readString();
        access = in.readString();
        radio = in.readString();
        gps = in.readString();
        forest = in.readString();
        mandays = in.readInt();
        created = (Timestamp) in.readValue(Timestamp.class.getClassLoader());
        if (in.readByte() == 0x01) {
            hazards = new ArrayList<String>();
            in.readList(hazards, String.class.getClassLoader());
        } else {
            hazards = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(details);
        dest.writeString(onlinePdfPath);
        dest.writeString(offlinePdfPath);
        dest.writeString(name);
        dest.writeString(target);
        dest.writeString(area);
        dest.writeString(access);
        dest.writeString(radio);
        dest.writeString(gps);
        dest.writeString(forest);
        dest.writeInt(mandays);
        dest.writeValue(created);
        if (hazards == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(hazards);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Sites> CREATOR = new Parcelable.Creator<Sites>() {
        @Override
        public Sites createFromParcel(Parcel in) {
            return new Sites(in);
        }

        @Override
        public Sites[] newArray(int size) {
            return new Sites[size];
        }
    };
}