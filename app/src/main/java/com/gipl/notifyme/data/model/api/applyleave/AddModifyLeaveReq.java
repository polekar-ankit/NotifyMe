package com.gipl.notifyme.data.model.api.applyleave;

import com.gipl.notifyme.data.model.api.BaseReq;
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

    @SerializedName("jLeaveStatus")
    @Expose
    private int jLeaveStatus;

    @SerializedName("sReason")
    @Expose
    private String sReason;

    @SerializedName("jLeaveFor")
    @Expose
    private int jLeaveFor;

    @SerializedName("suidLeaveType")
    @Expose
    private String suidLeaveType;

    @SerializedName("arrFilesBase64")
    @Expose
    private String[] arrFilesBase64;

    @SerializedName("sEmpCode")
    @Expose
    private String sEmpCode;

    public void setSuidPlant(String suidPlant) {
        this.suidPlant = suidPlant;
    }

    @SerializedName("suidPlant")
    @Expose
    private String suidPlant;

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

    public void setjLeaveStatus(int jLeaveStatus) {
        this.jLeaveStatus = jLeaveStatus;
    }

    public void setsReason(String sReason) {
        this.sReason = sReason;
    }

    public void setjLeaveFor(int jLeaveFor) {
        this.jLeaveFor = jLeaveFor;
    }

    public void setSuidLeaveType(String suidLeaveType) {
        this.suidLeaveType = suidLeaveType;
    }

    public void setArrFilesBase64(String[] arrFilesBase64) {
        this.arrFilesBase64 = arrFilesBase64;
    }

    public void setsEmpCode(String sEmpCode) {
        this.sEmpCode = sEmpCode;
    }
}
