package com.safe.macbros.treeandforest.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Tailgate implements Parcelable {
    private static final String TAG = "Tailgate";
    private String id;
    private String weather;
    private String taskId;
    private String taskName;
    private String blockId;
    private String blockName;
    private String blockOnlineUrl;
    private String blockOfflineUrl;
    private String driverId;
    private String emergencyProcedure;
    private String supervisorNote;
    private String startTime = "6:30am";
    private String finishTime = "1:00pm";
    private String mapUrl;
    private String plan;
    private String gps;
    private int driverPosition = 0;
    private String hours;
    private String issues;
    private Date date;
    private ArrayList<String> staff;
    private ArrayList<String> staffIds;
    private ArrayList<String> alertIds = new ArrayList<>();
    private ArrayList<String> visitorIds = new ArrayList<>();
    private Map<String, Object> tailgateMap = new HashMap<>();
    private Map<String, Object> staffChecks = new HashMap<>();
    private Timestamp timestamp;
    private boolean completed=false;
    private boolean workPass=false;
    private boolean driverPass=false;
    private boolean planPass=false;
    private boolean staffPass=false;
    private boolean hazPass=false;
    private boolean emergencyPass=false;
    private boolean issuesPass=false;
    private boolean superPass=false;
    private boolean visitorPass=false;

    public Tailgate() {

    }

    public Tailgate(String id, String weather, String taskId, String taskName, String blockId, String blockName, String blockOnlineUrl, String blockOfflineUrl, String driverId, String emergencyProcedure, String supervisorNote, String startTime, String finishTime, String mapUrl, String plan, String gps, Date date, ArrayList<String> staff, ArrayList<String> staffIds, ArrayList<String> alertIds, ArrayList<String> visitorIds, Map<String, Object> tailgateMap, Map<String, Object> staffChecks, Timestamp timestamp, boolean completed, boolean workPass, boolean driverPass, boolean planPass, boolean staffPass, boolean hazPass, boolean emergencyPass, boolean issuesPass, boolean superPass) {
        this.id = id;
        this.weather = weather;
        this.taskId = taskId;
        this.taskName = taskName;
        this.blockId = blockId;
        this.blockName = blockName;
        this.blockOnlineUrl = blockOnlineUrl;
        this.blockOfflineUrl = blockOfflineUrl;
        this.driverId = driverId;
        this.emergencyProcedure = emergencyProcedure;
        this.supervisorNote = supervisorNote;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.mapUrl = mapUrl;
        this.plan = plan;
        this.gps = gps;
        this.date = date;
        this.staff = staff;
        this.staffIds = staffIds;
        this.alertIds = alertIds;
        this.visitorIds = visitorIds;
        this.tailgateMap = tailgateMap;
        this.staffChecks = staffChecks;
        this.timestamp = timestamp;
        this.completed = completed;
        this.workPass = workPass;
        this.driverPass = driverPass;
        this.planPass = planPass;
        this.staffPass = staffPass;
        this.hazPass = hazPass;
        this.emergencyPass = emergencyPass;
        this.issuesPass = issuesPass;
        this.superPass = superPass;
    }

    public int getDriverPosition() {
        return driverPosition;
    }

    public void setDriverPosition(int driverPosition) {
        this.driverPosition = driverPosition;
    }

    public boolean isVisitorPass() {
        return visitorPass;
    }

    public void setVisitorPass(boolean visitorPass) {
        this.visitorPass = visitorPass;
    }

    public String getIssues() {
        return issues;
    }

    public void setIssues(String issues) {
        this.issues = issues;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
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

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getBlockOnlineUrl() {
        return blockOnlineUrl;
    }

    public void setBlockOnlineUrl(String blockOnlineUrl) {
        this.blockOnlineUrl = blockOnlineUrl;
    }

    public String getBlockOfflineUrl() {
        return blockOfflineUrl;
    }

    public void setBlockOfflineUrl(String blockOfflineUrl) {
        this.blockOfflineUrl = blockOfflineUrl;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getEmergencyProcedure() {
        return emergencyProcedure;
    }

    public void setEmergencyProcedure(String emergencyProcedure) {
        this.emergencyProcedure = emergencyProcedure;
    }

    public String getSupervisorNote() {
        return supervisorNote;
    }

    public void setSupervisorNote(String supervisorNote) {
        this.supervisorNote = supervisorNote;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getMapUrl() {
        return mapUrl;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<String> getStaff() {
        return staff;
    }

    public void setStaff(ArrayList<String> staff) {
        this.staff = staff;
    }

    public ArrayList<String> getStaffIds() {
        return staffIds;
    }

    public void setStaffIds(ArrayList<String> staffIds) {
        this.staffIds = staffIds;
    }

    public ArrayList<String> getAlertIds() {
        return alertIds;
    }

    public void setAlertIds(ArrayList<String> alertIds) {
        this.alertIds = alertIds;
    }

    public ArrayList<String> getVisitorIds() {
        return visitorIds;
    }

    public void setVisitorIds(ArrayList<String> visitorIds) {
        this.visitorIds = visitorIds;
    }

    public Map<String, Object> getTailgateMap() {
        return tailgateMap;
    }

    public void setTailgateMap(Map<String, Object> tailgateMap) {
        this.tailgateMap = tailgateMap;
    }

    public Map<String, Object> getStaffChecks() {
        return staffChecks;
    }

    public void setStaffChecks(Map<String, Object> staffChecks) {
        this.staffChecks = staffChecks;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isWorkPass() {
        return workPass;
    }

    public void setWorkPass(boolean workPass) {
        this.workPass = workPass;
    }

    public boolean isDriverPass() {
        return driverPass;
    }

    public void setDriverPass(boolean driverPass) {
        this.driverPass = driverPass;
    }

    public boolean isPlanPass() {
        return planPass;
    }

    public void setPlanPass(boolean planPass) {
        this.planPass = planPass;
    }

    public boolean isStaffPass() {
        return staffPass;
    }

    public void setStaffPass(boolean staffPass) {
        this.staffPass = staffPass;
    }

    public boolean isHazPass() {
        return hazPass;
    }

    public void setHazPass(boolean hazPass) {
        this.hazPass = hazPass;
    }

    public boolean isEmergencyPass() {
        return emergencyPass;
    }

    public void setEmergencyPass(boolean emergencyPass) {
        this.emergencyPass = emergencyPass;
    }

    public boolean isIssuesPass() {
        return issuesPass;
    }

    public void setIssuesPass(boolean issuesPass) {
        this.issuesPass = issuesPass;
    }

    public boolean isSuperPass() {
        return superPass;
    }

    public void setSuperPass(boolean superPass) {
        this.superPass = superPass;
    }

    protected Tailgate(Parcel in) {
        id = in.readString();
        weather = in.readString();
        taskId = in.readString();
        taskName = in.readString();
        blockId = in.readString();
        blockName = in.readString();
        blockOnlineUrl = in.readString();
        blockOfflineUrl = in.readString();
        driverId = in.readString();
        emergencyProcedure = in.readString();
        supervisorNote = in.readString();
        startTime = in.readString();
        finishTime = in.readString();
        mapUrl = in.readString();
        plan = in.readString();
        gps = in.readString();
        long tmpDate = in.readLong();
        date = tmpDate != -1 ? new Date(tmpDate) : null;
        if (in.readByte() == 0x01) {
            staff = new ArrayList<String>();
            in.readList(staff, String.class.getClassLoader());
        } else {
            staff = null;
        }
        if (in.readByte() == 0x01) {
            staffIds = new ArrayList<String>();
            in.readList(staffIds, String.class.getClassLoader());
        } else {
            staffIds = null;
        }
        if (in.readByte() == 0x01) {
            alertIds = new ArrayList<String>();
            in.readList(alertIds, String.class.getClassLoader());
        } else {
            alertIds = null;
        }
        if (in.readByte() == 0x01) {
            visitorIds = new ArrayList<String>();
            in.readList(visitorIds, String.class.getClassLoader());
        } else {
            visitorIds = null;
        }
        timestamp = (Timestamp) in.readValue(Timestamp.class.getClassLoader());
        completed = in.readByte() != 0x00;
        workPass = in.readByte() != 0x00;
        driverPass = in.readByte() != 0x00;
        planPass = in.readByte() != 0x00;
        staffPass = in.readByte() != 0x00;
        hazPass = in.readByte() != 0x00;
        emergencyPass = in.readByte() != 0x00;
        issuesPass = in.readByte() != 0x00;
        superPass = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(weather);
        dest.writeString(taskId);
        dest.writeString(taskName);
        dest.writeString(blockId);
        dest.writeString(blockName);
        dest.writeString(blockOnlineUrl);
        dest.writeString(blockOfflineUrl);
        dest.writeString(driverId);
        dest.writeString(emergencyProcedure);
        dest.writeString(supervisorNote);
        dest.writeString(startTime);
        dest.writeString(finishTime);
        dest.writeString(mapUrl);
        dest.writeString(plan);
        dest.writeString(gps);
        dest.writeLong(date != null ? date.getTime() : -1L);
        if (staff == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(staff);
        }
        if (staffIds == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(staffIds);
        }
        if (alertIds == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(alertIds);
        }
        if (visitorIds == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(visitorIds);
        }
        dest.writeValue(timestamp);
        dest.writeByte((byte) (completed ? 0x01 : 0x00));
        dest.writeByte((byte) (workPass ? 0x01 : 0x00));
        dest.writeByte((byte) (driverPass ? 0x01 : 0x00));
        dest.writeByte((byte) (planPass ? 0x01 : 0x00));
        dest.writeByte((byte) (staffPass ? 0x01 : 0x00));
        dest.writeByte((byte) (hazPass ? 0x01 : 0x00));
        dest.writeByte((byte) (emergencyPass ? 0x01 : 0x00));
        dest.writeByte((byte) (issuesPass ? 0x01 : 0x00));
        dest.writeByte((byte) (superPass ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Tailgate> CREATOR = new Parcelable.Creator<Tailgate>() {
        @Override
        public Tailgate createFromParcel(Parcel in) {
            return new Tailgate(in);
        }

        @Override
        public Tailgate[] newArray(int size) {
            return new Tailgate[size];
        }
    };
}