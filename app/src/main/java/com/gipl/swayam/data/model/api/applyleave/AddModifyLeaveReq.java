package com.gipl.swayam.data.model.api.applyleave;

import com.gipl.swayam.data.model.api.BaseReq;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddModifyLeaveReq extends BaseReq {
    @SerializedName("suidUser")
    @Expose
    private String suidUser;

    @SerializedName("suidLeaveRequest")
    @Expose
    private String suidLeaveRequest;

    @SerializedName("sFrom")
    @Expose
    private String sFrom;

    @SerializedName("sTo")
    @Expose
    private String sTo;

    @SerializedName("suidLeaveType")
    @Expose
    private String suidLeaveType;

    @SerializedName("jLeaveFor")
    @Expose
    private int jLeaveFor;

    @SerializedName("jLeaveTo")
    @Expose
    private int jLeaveTo;

    @SerializedName("dDays")
    @Expose
    private double days;

    @SerializedName("sReason")
    @Expose
    private String sReason;

    @SerializedName("arrFilesBase64")
    @Expose
    private String[] arrFilesBase64;

    @SerializedName("suidPlant")
    @Expose
    private String suidPlant;

    @SerializedName("suidUserApplicant")
    @Expose
    private String suidUserApplicant;

//    @SerializedName("jLeaveStatus")
//    @Expose
//    private int jLeaveStatus;
//
//    @SerializedName("sEmpCode")
//    @Expose
//    private String sEmpCode;


    public void setjLeaveTo(int jLeaveTo) {
        this.jLeaveTo = jLeaveTo;
    }

    public void setDays(double days) {
        this.days = days;
    }

    public void setSuidPlant(String suidPlant) {
        this.suidPlant = suidPlant;
    }

    public void setSuidUserApplicant(String suidUserApplicant) {
        this.suidUserApplicant = suidUserApplicant;
    }

    public void setSuidUser(String suidUser) {
        this.suidUser = suidUser;
    }

    public void setSuidLeaveRequest(String suidLeaveRequest) {
        this.suidLeaveRequest = suidLeaveRequest;
    }

    public void setsFrom(String sFrom) {
        this.sFrom = sFrom;
    }

    public void setsTo(String sTo) {
        this.sTo = sTo;
    }

//    public void setjLeaveStatus(int jLeaveStatus) {
//        this.jLeaveStatus = jLeaveStatus;
//    }

    public void setjLeaveFor(int jLeaveFor) {
        this.jLeaveFor = jLeaveFor;
    }

    public void setSuidLeaveType(String suidLeaveType) {
        this.suidLeaveType = suidLeaveType;
    }

    public void setArrFilesBase64(String[] arrFilesBase64) {
        this.arrFilesBase64 = arrFilesBase64;
    }

//    public void setsEmpCode(String sEmpCode) {
//        this.sEmpCode = sEmpCode;
//    }

    public String getsReason() {
        return sReason;
    }

    public void setsReason(String sReason) {
//        this.sReason = sReason;
        this.sReason = sReason.replaceAll("(\\s+)", " ");
    }
}
