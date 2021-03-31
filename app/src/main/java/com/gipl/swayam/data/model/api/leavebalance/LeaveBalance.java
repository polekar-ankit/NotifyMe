package com.gipl.swayam.data.model.api.leavebalance;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeaveBalance {
    @SerializedName("suidUser")
    @Expose
    private String suidUser;
    @SerializedName("suidApplicantUser")
    @Expose
    private String suidApplicantUser;
    @SerializedName("sLeaveType")
    @Expose
    private String sLeaveType;
    @SerializedName("dNoOfDays")
    @Expose
    private Double dNoOfDays;
    @SerializedName("suidLeaveBalance")
    @Expose
    private String suidLeaveBalance;
    @SerializedName("jStatus")
    @Expose
    private Integer jStatus;

    public String getSuidUser() {
        return suidUser;
    }

    public void setSuidUser(String suidUser) {
        this.suidUser = suidUser;
    }

    public String getSuidApplicantUser() {
        return suidApplicantUser;
    }

    public void setSuidApplicantUser(String suidApplicantUser) {
        this.suidApplicantUser = suidApplicantUser;
    }

    public String getLeaveType() {
        return sLeaveType;
    }

    public void setSLeaveType(String sLeaveType) {
        this.sLeaveType = sLeaveType;
    }

    public Double getNoOfDays() {
        return dNoOfDays;
    }

    public void setDNoOfDays(Double dNoOfDays) {
        this.dNoOfDays = dNoOfDays;
    }

    public String getSuidLeaveBalance() {
        return suidLeaveBalance;
    }

    public void setSuidLeaveBalance(String suidLeaveBalance) {
        this.suidLeaveBalance = suidLeaveBalance;
    }

    public Integer getJStatus() {
        return jStatus;
    }

    public void setJStatus(Integer jStatus) {
        this.jStatus = jStatus;
    }

}
