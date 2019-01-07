package com.safe.macbros.treeandforest.Activities.tailgate.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;
import java.util.ArrayList;

public class MonthlySafety implements Parcelable {
    private static final String TAG = "MonthlyMeetings";

    ArrayList<String> meetingAttendance;
    String id;
    String previousID;
    ArrayList<String> hazardsAndControls;
    ArrayList<String> nearhits;
    ArrayList<String> training;
    ArrayList<String> audits;
    String tailgateId;
    String siteId;
    String emergencyDrillId;
    ArrayList<String> actionsList;
    com.google.firebase.Timestamp mTimestamp;

    public MonthlySafety(){}

    public MonthlySafety(ArrayList<String> meetingAttendance, String id, String previousID, ArrayList<String> hazardsAndControls, ArrayList<String> nearhits, ArrayList<String> training, ArrayList<String> audits, String tailgateId, String siteId, String emergencyDrillId, ArrayList<String> actionsList, com.google.firebase.Timestamp timestamp) {
        this.meetingAttendance = meetingAttendance;
        this.id = id;
        this.previousID = previousID;
        this.hazardsAndControls = hazardsAndControls;
        this.nearhits = nearhits;
        this.training = training;
        this.audits = audits;
        this.tailgateId = tailgateId;
        this.siteId = siteId;
        this.emergencyDrillId = emergencyDrillId;
        this.actionsList = actionsList;
        mTimestamp = timestamp;
    }

    public static String getTAG() {
        return TAG;
    }

    public ArrayList<String> getMeetingAttendance() {
        return meetingAttendance;
    }

    public void setMeetingAttendance(ArrayList<String> meetingAttendance) {
        this.meetingAttendance = meetingAttendance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPreviousID() {
        return previousID;
    }

    public void setPreviousID(String previousID) {
        this.previousID = previousID;
    }

    public ArrayList<String> getHazardsAndControls() {
        return hazardsAndControls;
    }

    public void setHazardsAndControls(ArrayList<String> hazardsAndControls) {
        this.hazardsAndControls = hazardsAndControls;
    }

    public ArrayList<String> getNearhits() {
        return nearhits;
    }

    public void setNearhits(ArrayList<String> nearhits) {
        this.nearhits = nearhits;
    }

    public ArrayList<String> getTraining() {
        return training;
    }

    public void setTraining(ArrayList<String> training) {
        this.training = training;
    }

    public ArrayList<String> getAudits() {
        return audits;
    }

    public void setAudits(ArrayList<String> audits) {
        this.audits = audits;
    }

    public String getTailgateId() {
        return tailgateId;
    }

    public void setTailgateId(String tailgateId) {
        this.tailgateId = tailgateId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getEmergencyDrillId() {
        return emergencyDrillId;
    }

    public void setEmergencyDrillId(String emergencyDrillId) {
        this.emergencyDrillId = emergencyDrillId;
    }

    public ArrayList<String> getActionsList() {
        return actionsList;
    }

    public void setActionsList(ArrayList<String> actionsList) {
        this.actionsList = actionsList;
    }

    public com.google.firebase.Timestamp getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(com.google.firebase.Timestamp timestamp) {
        mTimestamp = timestamp;
    }

    protected MonthlySafety(Parcel in) {
        if (in.readByte() == 0x01) {
            meetingAttendance = new ArrayList<String>();
            in.readList(meetingAttendance, String.class.getClassLoader());
        } else {
            meetingAttendance = null;
        }
        id = in.readString();
        previousID = in.readString();
        if (in.readByte() == 0x01) {
            hazardsAndControls = new ArrayList<String>();
            in.readList(hazardsAndControls, String.class.getClassLoader());
        } else {
            hazardsAndControls = null;
        }
        if (in.readByte() == 0x01) {
            nearhits = new ArrayList<String>();
            in.readList(nearhits, String.class.getClassLoader());
        } else {
            nearhits = null;
        }
        if (in.readByte() == 0x01) {
            training = new ArrayList<String>();
            in.readList(training, String.class.getClassLoader());
        } else {
            training = null;
        }
        if (in.readByte() == 0x01) {
            audits = new ArrayList<String>();
            in.readList(audits, String.class.getClassLoader());
        } else {
            audits = null;
        }
        tailgateId = in.readString();
        siteId = in.readString();
        emergencyDrillId = in.readString();
        if (in.readByte() == 0x01) {
            actionsList = new ArrayList<String>();
            in.readList(actionsList, String.class.getClassLoader());
        } else {
            actionsList = null;
        }
        mTimestamp = (com.google.firebase.Timestamp) in.readValue(com.google.firebase.Timestamp.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (meetingAttendance == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(meetingAttendance);
        }
        dest.writeString(id);
        dest.writeString(previousID);
        if (hazardsAndControls == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(hazardsAndControls);
        }
        if (nearhits == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(nearhits);
        }
        if (training == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(training);
        }
        if (audits == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(audits);
        }
        dest.writeString(tailgateId);
        dest.writeString(siteId);
        dest.writeString(emergencyDrillId);
        if (actionsList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(actionsList);
        }
        dest.writeValue(mTimestamp);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MonthlySafety> CREATOR = new Parcelable.Creator<MonthlySafety>() {
        @Override
        public MonthlySafety createFromParcel(Parcel in) {
            return new MonthlySafety(in);
        }

        @Override
        public MonthlySafety[] newArray(int size) {
            return new MonthlySafety[size];
        }
    };
}