package com.gipl.notifyme.data.model.firebase;

import com.gipl.notifyme.uility.TimeUtility;
import com.google.firebase.database.DataSnapshot;

public class Employee {
    private int checkType;
    private String suidShift;
    private long checkTime;
    private long dtModified;
    private int checkBy;
    private int checkOutType;
    private String displayCheckTime;
    public Employee(DataSnapshot snapshot) {
        this.checkType = Integer.parseInt(snapshot.child("checkType").getValue().toString());
        this.suidShift = snapshot.child("suidShift").getValue().toString();
        this.checkTime = Long.parseLong(snapshot.child("checkTime").getValue().toString());
        this.checkBy = Integer.parseInt(snapshot.child("checkBy").getValue().toString());
        this.checkOutType = Integer.parseInt(snapshot.child("checkOutType").getValue().toString());
        this.displayCheckTime = snapshot.child("displayCheckTime").getValue().toString();
        if (snapshot.hasChild("dtModified"))
            this.dtModified = Long.parseLong(snapshot.child("dtModified").getValue().toString());
    }
    public Employee(int checkType, String suidShift, long checkTime, int checkBy, int checkOutType,long dtModified) {
        this.checkType = checkType;
        this.suidShift = suidShift;
        this.checkTime = checkTime;
        this.checkBy = checkBy;
        this.checkOutType = checkOutType;
        this.displayCheckTime = TimeUtility.convertUtcMillisecondToTime(dtModified);
        this.dtModified = dtModified;
    }

    public long getDtModified() {
        return dtModified;
    }

    public void setDtModified(long dtModified) {
        this.dtModified = dtModified;
    }

    public int getCheckOutType() {
        return checkOutType;
    }

    public void setCheckOutType(int checkOutType) {
        this.checkOutType = checkOutType;
    }

    public String getDisplayCheckTime() {
        return displayCheckTime;
    }

    public void setDisplayCheckTime(String displayCheckTime) {
        this.displayCheckTime = displayCheckTime;
    }

    public int getCheckBy() {
        return checkBy;
    }

    public void setCheckBy(int checkBy) {
        this.checkBy = checkBy;
    }

    public int getCheckType() {
        return checkType;
    }

    public void setCheckType(int checkType) {
        this.checkType = checkType;
    }

    public String getSuidShift() {
        return suidShift;
    }

    public void setSuidShift(String suidShift) {
        this.suidShift = suidShift;
    }

    public long getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(long checkTime) {
        this.checkTime = checkTime;
    }
}
