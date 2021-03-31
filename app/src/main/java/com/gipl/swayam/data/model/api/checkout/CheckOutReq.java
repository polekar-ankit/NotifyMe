package com.gipl.swayam.data.model.api.checkout;

import com.gipl.swayam.data.model.api.BaseReq;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckOutReq extends BaseReq {
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

    @SerializedName("jCheckOutType")
    @Expose
    private int checkOutType;

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

    public void setCheckOutType(int checkOutType) {
        this.checkOutType = checkOutType;
    }
}
