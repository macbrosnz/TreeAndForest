package com.safe.macbros.treeandforest.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.ArrayList;
@IgnoreExtraProperties
public class Equipment implements Parcelable {
    private static final String TAG = "EquipmentMain";

    private String id;
    private String maintenanceUrl;
    private String onlineImageUrl;
    private String offlineImageUrl;
    private String hazardId;
    private String parentId;
    private ArrayList<String> taskId;
    private String maintenanceHint;
    private String name;
    private String standards;
    private String onlineStandardsUrl;
    private String offlineStandardsUrl;
    private Boolean ppe;
    private Boolean chemical;
    private Boolean firstaid;
    private Boolean vehicle;
    private ArrayList<String> sublist;
    private ArrayList<String> childlist;


    public Equipment(){
        //empty constructor for loading into
    }

    public Equipment(String id, String maintenanceUrl, String onlineImageUrl, String offlineImageUrl, String hazardId, String parentId, ArrayList<String> taskId, String maintenanceHint, String name, String standards, String onlineStandardsUrl, String offlineStandardsUrl, Boolean ppe, Boolean chemical, Boolean firstaid, Boolean vehicle, ArrayList<String> sublist, ArrayList<String> childlist) {
        this.id = id;
        this.maintenanceUrl = maintenanceUrl;
        this.onlineImageUrl = onlineImageUrl;
        this.offlineImageUrl = offlineImageUrl;
        this.hazardId = hazardId;
        this.parentId = parentId;
        this.taskId = taskId;
        this.maintenanceHint = maintenanceHint;
        this.name = name;
        this.standards = standards;
        this.onlineStandardsUrl = onlineStandardsUrl;
        this.offlineStandardsUrl = offlineStandardsUrl;
        this.ppe = ppe;
        this.chemical = chemical;
        this.firstaid = firstaid;
        this.vehicle = vehicle;
        this.sublist = sublist;
        this.childlist = childlist;
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

    public String getMaintenanceUrl() {
        return maintenanceUrl;
    }

    public void setMaintenanceUrl(String maintenanceUrl) {
        this.maintenanceUrl = maintenanceUrl;
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

    public String getHazardId() {
        return hazardId;
    }

    public void setHazardId(String hazardId) {
        this.hazardId = hazardId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public ArrayList<String> getTaskId() {
        return taskId;
    }

    public void setTaskId(ArrayList<String> taskId) {
        this.taskId = taskId;
    }

    public String getMaintenanceHint() {
        return maintenanceHint;
    }

    public void setMaintenanceHint(String maintenanceHint) {
        this.maintenanceHint = maintenanceHint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStandards() {
        return standards;
    }

    public void setStandards(String standards) {
        this.standards = standards;
    }

    public String getOnlineStandardsUrl() {
        return onlineStandardsUrl;
    }

    public void setOnlineStandardsUrl(String onlineStandardsUrl) {
        this.onlineStandardsUrl = onlineStandardsUrl;
    }

    public String getOfflineStandardsUrl() {
        return offlineStandardsUrl;
    }

    public void setOfflineStandardsUrl(String offlineStandardsUrl) {
        this.offlineStandardsUrl = offlineStandardsUrl;
    }

    public Boolean getPpe() {
        return ppe;
    }

    public void setPpe(Boolean ppe) {
        this.ppe = ppe;
    }

    public Boolean getChemical() {
        return chemical;
    }

    public void setChemical(Boolean chemical) {
        this.chemical = chemical;
    }

    public Boolean getFirstaid() {
        return firstaid;
    }

    public void setFirstaid(Boolean firstaid) {
        this.firstaid = firstaid;
    }

    public Boolean getVehicle() {
        return vehicle;
    }

    public void setVehicle(Boolean vehicle) {
        this.vehicle = vehicle;
    }

    public ArrayList<String> getSublist() {
        return sublist;
    }

    public void setSublist(ArrayList<String> sublist) {
        this.sublist = sublist;
    }

    public ArrayList<String> getChildlist() {
        return childlist;
    }

    public void setChildlist(ArrayList<String> childlist) {
        this.childlist = childlist;
    }

    protected Equipment(Parcel in) {
        id = in.readString();
        maintenanceUrl = in.readString();
        onlineImageUrl = in.readString();
        offlineImageUrl = in.readString();
        hazardId = in.readString();
        parentId = in.readString();
        if (in.readByte() == 0x01) {
            taskId = new ArrayList<String>();
            in.readList(taskId, String.class.getClassLoader());
        } else {
            taskId = null;
        }
        maintenanceHint = in.readString();
        name = in.readString();
        standards = in.readString();
        onlineStandardsUrl = in.readString();
        offlineStandardsUrl = in.readString();
        byte ppeVal = in.readByte();
        ppe = ppeVal == 0x02 ? null : ppeVal != 0x00;
        byte chemicalVal = in.readByte();
        chemical = chemicalVal == 0x02 ? null : chemicalVal != 0x00;
        byte firstaidVal = in.readByte();
        firstaid = firstaidVal == 0x02 ? null : firstaidVal != 0x00;
        byte vehicleVal = in.readByte();
        vehicle = vehicleVal == 0x02 ? null : vehicleVal != 0x00;
        if (in.readByte() == 0x01) {
            sublist = new ArrayList<String>();
            in.readList(sublist, String.class.getClassLoader());
        } else {
            sublist = null;
        }
        if (in.readByte() == 0x01) {
            childlist = new ArrayList<String>();
            in.readList(childlist, String.class.getClassLoader());
        } else {
            childlist = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(maintenanceUrl);
        dest.writeString(onlineImageUrl);
        dest.writeString(offlineImageUrl);
        dest.writeString(hazardId);
        dest.writeString(parentId);
        if (taskId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(taskId);
        }
        dest.writeString(maintenanceHint);
        dest.writeString(name);
        dest.writeString(standards);
        dest.writeString(onlineStandardsUrl);
        dest.writeString(offlineStandardsUrl);
        if (ppe == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (ppe ? 0x01 : 0x00));
        }
        if (chemical == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (chemical ? 0x01 : 0x00));
        }
        if (firstaid == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (firstaid ? 0x01 : 0x00));
        }
        if (vehicle == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (vehicle ? 0x01 : 0x00));
        }
        if (sublist == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(sublist);
        }
        if (childlist == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(childlist);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Equipment> CREATOR = new Parcelable.Creator<Equipment>() {
        @Override
        public Equipment createFromParcel(Parcel in) {
            return new Equipment(in);
        }

        @Override
        public Equipment[] newArray(int size) {
            return new Equipment[size];
        }
    };
}