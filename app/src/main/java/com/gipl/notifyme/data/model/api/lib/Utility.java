package com.gipl.notifyme.data.model.api.lib;

import com.gipl.notifyme.data.model.api.lib.utility.CheckInType;
import com.gipl.notifyme.data.model.api.lib.utility.CheckOutType;
import com.gipl.notifyme.data.model.api.lib.utility.CheckType;
import com.gipl.notifyme.data.model.api.lib.utility.LeaveFor;
import com.gipl.notifyme.data.model.api.lib.utility.LeaveStatus;
import com.gipl.notifyme.data.model.api.lib.utility.StatusType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Utility {
    @SerializedName("CHECKOUTTYPE")
    @Expose
    private CheckOutType checkOutType;

    @SerializedName("CHECKINOUTYPE")
    @Expose
    private CheckInType checkInType;

    @SerializedName("LEAVESTATUS")
    @Expose
    private LeaveStatus leaveStatus;

    @SerializedName("LEAVEFOR")
    @Expose
    private LeaveFor leaveFor;
    @SerializedName("STATUSTYPE")
    @Expose
    private StatusType statusType;
    @SerializedName("CHECKTYPE")
    @Expose
    private final CheckType checkType = new CheckType();

    public StatusType getStatusType() {
        return statusType;
    }

    public CheckType getCheckType() {
        return checkType;
    }

    public LeaveFor getLeaveFor() {
        return leaveFor;
    }

    public LeaveStatus getLeaveStatus() {
        return leaveStatus;
    }

    public void setLeaveStatus(LeaveStatus leaveStatus) {
        this.leaveStatus = leaveStatus;
    }

    public CheckOutType getCheckOutType() {
        return checkOutType;
    }

    public void setCheckOutType(CheckOutType checkOutType) {
        this.checkOutType = checkOutType;
    }

    public CheckInType getCheckInType() {
        return checkInType;
    }

    public void setCheckInType(CheckInType checkInType) {
        this.checkInType = checkInType;
    }
}
