package com.gipl.notifyme.data.model.api.punchingslip;

import com.gipl.notifyme.data.model.api.BaseReq;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddPunchingSlipReq extends BaseReq {
    @SerializedName("suidMissPunch")
    @Expose
    private String suidMissPunch;

    @SerializedName("sEmpCode")
    @Expose
    private String empCode;

    @SerializedName("suidUser")
    @Expose
    private String suidUser;

    @SerializedName("sDtMissPunch")
    @Expose
    private String dtMissPunch;

    @SerializedName("suidShift")
    @Expose
    private String suidShift;

    @SerializedName("sInTime")
    @Expose
    private String inTime;
    @SerializedName("sOutTime")
    @Expose
    private String outTime;

    @SerializedName("suidUserAplicant")
    @Expose
    private String suidUserAplicant;
    @SerializedName("sExtraInfo")
    @Expose
    private String sExtraInfo;
    @SerializedName("sReason")
    @Expose
    private String sReason;

    public AddPunchingSlipReq() {
    }

    public void setsReason(String sReason) {
        this.sReason = sReason;
    }

    public void setsExtraInfo(String sExtraInfo) {
        this.sExtraInfo = sExtraInfo;
    }

    public void setSuidMissPunch(String suidMissPunch) {
        this.suidMissPunch = suidMissPunch;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public void setSuidUser(String suidUser) {
        this.suidUser = suidUser;
    }

    public void setDtMissPunch(String dtMissPunch) {
        this.dtMissPunch = dtMissPunch;
    }

    public void setSuidShift(String suidShift) {
        this.suidShift = suidShift;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public void setSuidUserAplicant(String suidUserAplicant) {
        this.suidUserAplicant = suidUserAplicant;
    }
}
