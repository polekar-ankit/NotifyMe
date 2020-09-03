package com.gipl.notifyme.data.model.api.checkin;

import com.gipl.notifyme.data.model.api.BaseReq;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckInReq extends BaseReq {

    @SerializedName("suidShift")
    @Expose
    private String suidShift;

    @SerializedName("sAttendanceDate")
    @Expose
    private String attendanceDate;

    @SerializedName("sCheckTime")
    @Expose
    private String checkTime;

    @SerializedName("jCheckBy")
    @Expose
    private int checkBy;

    public void setSuidShift(String suidShift) {
        this.suidShift = suidShift;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public void setCheckBy(int checkBy) {
        this.checkBy = checkBy;
    }
}
