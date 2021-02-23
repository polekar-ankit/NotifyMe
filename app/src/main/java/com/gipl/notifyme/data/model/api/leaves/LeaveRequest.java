package com.gipl.notifyme.data.model.api.leaves;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeaveRequest {
    @SerializedName("suidLeaveRequest")
    @Expose
    public String suidLeaveRequest;
    @SerializedName("arrURLs")
    @Expose
    private String[] arrURLs;
    @SerializedName("sEmpCode")
    @Expose
    private String empCode;
    @SerializedName("sEmpName")
    @Expose
    private String empName;
    @SerializedName("sLeaveDates")
    @Expose
    private String leaveDates;
    @SerializedName("sLeaveStatus")
    @Expose
    private String leaveStatusName;
    @SerializedName("sLeaveType")
    @Expose
    private String leaveType;
    @SerializedName("sReason")
    @Expose
    private String reason;
    @SerializedName("jLeaveStatus")
    @Expose
    private int leaveStatus;
    @SerializedName("dDays")
    @Expose
    private double days;
    @SerializedName("sComment")
    @Expose
    private String comment;

    public int getColor() {
        return color;
    }

    @Expose(serialize = false,deserialize = false)
    private int color;

    public String getLeaveStatusName() {
        return leaveStatusName;
    }

    public String getSuidLeaveRequest() {
        return suidLeaveRequest;
    }

    public String[] getArrURLs() {
        return arrURLs;
    }

    public String getEmpCode() {
        return empCode;
    }

    public String getEmpName() {
        return empName;
    }

    public String getLeaveDates() {
        return leaveDates;
    }



    public String getLeaveType() {
        return leaveType;
    }

    public String getReason() {
        return reason;
    }

    public int getLeaveStatus() {
        return leaveStatus;
    }

    public double getDays() {
        return days;
    }

    public String getComment() {
        return comment;
    }


    public void setColor(int color) {
        this.color = color;
    }
}
