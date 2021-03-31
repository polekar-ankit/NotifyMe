package com.gipl.swayam.data.model.api.addovertime;

import com.gipl.swayam.data.model.api.BaseReq;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddOverTimeReq extends BaseReq {
    @SerializedName("suidOverTime")
    @Expose
    private String suidOverTime;

    @SerializedName("sEmpCode")
    @Expose
    private String empCode;

    @SerializedName("suidUser")
    @Expose
    private String suidUser;

    @SerializedName("sDtOverTime")
    @Expose
    private String dtOverTime;

    @SerializedName("suidPlant")
    @Expose
    private String suidPlant;

    @SerializedName("rOTHrs")
    @Expose
    private double oTHrs;

    @SerializedName("rIntesiveHrs")
    @Expose
    private double intesiveHrs;

    @SerializedName("suidUserAplicant")
    @Expose
    private String suidUserAplicant;

    @SerializedName("sReason")
    @Expose
    private String sReason;

    @SerializedName("sExtraInfo")
    @Expose
    private String sExtraInfo;

    public void setSuidOverTime(String suidOverTime) {
        this.suidOverTime = suidOverTime;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public void setSuidUser(String suidUser) {
        this.suidUser = suidUser;
    }

    public void setDtOverTime(String dtOverTime) {
        this.dtOverTime = dtOverTime;
    }

    public void setSuidPlant(String suidPlant) {
        this.suidPlant = suidPlant;
    }

    public void setoTHrs(double oTHrs) {
        this.oTHrs = oTHrs;
    }

    public void setIntesiveHrs(double intesiveHrs) {
        this.intesiveHrs = intesiveHrs;
    }

    public void setSuidUserAplicant(String suidUserAplicant) {
        this.suidUserAplicant = suidUserAplicant;
    }

    public void setsReason(String sReason) {
//        this.sReason = sReason;
        this.sReason = sReason.replaceAll("(\\s+)", " ");
    }

    public void setsExtraInfo(String sExtraInfo) {
        this.sExtraInfo = sExtraInfo;
    }
}
