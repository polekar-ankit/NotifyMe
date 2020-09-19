package com.gipl.notifyme.data.model.api.leavetype;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeaveApproval {
    @SerializedName("jLeaveType")
    @Expose
    private Integer jLeaveType;
    @SerializedName("sLeaveType")
    @Expose
    private String sLeaveType;
    @SerializedName("suidLeaveApproval")
    @Expose
    private String suidLeaveApproval;
    @SerializedName("sDescription")
    @Expose
    private String sDescription;
    @SerializedName("sApprovalsRequired")
    @Expose
    private Integer sApprovalsRequired;

    public LeaveApproval(String sLeaveType) {
        this.sLeaveType = sLeaveType;
    }

    public String getsDescription() {
        return sDescription;
    }

    @Override
    public String toString() {
        return sDescription != null ? sLeaveType + " (" + sDescription + ")" : sLeaveType;
    }

    public Integer getJLeaveType() {
        return jLeaveType;
    }

    public void setJLeaveType(Integer jLeaveType) {
        this.jLeaveType = jLeaveType;
    }

    public String getSLeaveType() {
        return sLeaveType;
    }

    public void setSLeaveType(String sLeaveType) {
        this.sLeaveType = sLeaveType;
    }

    public String getSuidLeaveApproval() {
        return suidLeaveApproval;
    }

    public void setSuidLeaveApproval(String suidLeaveApproval) {
        this.suidLeaveApproval = suidLeaveApproval;
    }

    public Integer getSApprovalsRequired() {
        return sApprovalsRequired;
    }

    public void setSApprovalsRequired(Integer sApprovalsRequired) {
        this.sApprovalsRequired = sApprovalsRequired;
    }

}
