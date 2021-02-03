package com.gipl.notifyme.data.model.api.shiftchange;

import com.gipl.notifyme.data.model.api.BaseReq;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShiftChangeReq extends BaseReq {
    @SerializedName("suidSCR")
    @Expose
    String suidSCR="";

    @SerializedName("sEmpCode")
    @Expose
    String sEmpCode;

    @SerializedName("suidEmployee")
    @Expose
    String suidEmployee;

    @SerializedName("sDtShiftFrom")
    @Expose
    String dtShiftFrom;

    @SerializedName("sDtShiftTo")
    @Expose
    String dtShiftTo;

    @SerializedName("suidPlant")
    @Expose
    String suidPlant;

    @SerializedName("suidShiftFrom")
    @Expose
    String suidShiftFrom;

    @SerializedName("suidShiftTo")
    @Expose
    String suidShiftTo;

    @SerializedName("suidUserAplicant")
    @Expose
    String suidUserAplicant;

    @SerializedName("sReason")
    @Expose
    String reason;

    @SerializedName("sExtraInfo")
    @Expose
    String sExtraInfo;

    public void setSuidSCR(String suidSCR) {
        this.suidSCR = suidSCR;
    }

    public void setsEmpCode(String sEmpCode) {
        this.sEmpCode = sEmpCode;
    }

    public void setSuidEmployee(String suidEmployee) {
        this.suidEmployee = suidEmployee;
    }

    public void setDtShiftFrom(String dtShiftFrom) {
        this.dtShiftFrom = dtShiftFrom;
    }

    public void setDtShiftTo(String dtShiftTo) {
        this.dtShiftTo = dtShiftTo;
    }

    public void setSuidPlant(String suidPlant) {
        this.suidPlant = suidPlant;
    }

    public void setSuidShiftFrom(String suidShiftFrom) {
        this.suidShiftFrom = suidShiftFrom;
    }

    public void setSuidShiftTo(String suidShiftTo) {
        this.suidShiftTo = suidShiftTo;
    }

    public void setSuidUserAplicant(String suidUserAplicant) {
        this.suidUserAplicant = suidUserAplicant;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setsExtraInfo(String sExtraInfo) {
        this.sExtraInfo = sExtraInfo;
    }
}
