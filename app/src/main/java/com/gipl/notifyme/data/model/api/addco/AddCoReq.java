package com.gipl.notifyme.data.model.api.addco;

import com.gipl.notifyme.data.model.api.BaseReq;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddCoReq extends BaseReq {
    @SerializedName("suidCO")
    @Expose
    private String suidCO="";

    @SerializedName("sEmpCode")
    @Expose
    private String empCode;

    @SerializedName("suidUser")
    @Expose
    private String suidUser;

    @SerializedName("sDtCO")
    @Expose
    private String sDtCO;

    @SerializedName("rHours")
    @Expose
    private int rHours=0;

    @SerializedName("jCOFor")
    @Expose
    private int jCOFor;

    @SerializedName("suidUserAplicant")
    @Expose
    private String suidUserAplicant;

    @SerializedName("sReason")
    @Expose
    private String sReason;

    @SerializedName("sExtraInfo")
    @Expose
    private String sExtraInfo="";

    public void setSuidCO(String suidCO) {
        this.suidCO = suidCO;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public void setSuidUser(String suidUser) {
        this.suidUser = suidUser;
    }

    public void setsDtCO(String sDtCO) {
        this.sDtCO = sDtCO;
    }

    public void setrHours(int rHours) {
        this.rHours = rHours;
    }

    public void setjCOFor(int jCOFor) {
        this.jCOFor = jCOFor;
    }

    public void setSuidUserAplicant(String suidUserAplicant) {
        this.suidUserAplicant = suidUserAplicant;
    }

    public void setsReason(String sReason) {
//        this.sReason = sReason;
        this.sReason = sReason.replaceAll("(\\s+)", "\n");
    }

    public void setsExtraInfo(String sExtraInfo) {
        this.sExtraInfo = sExtraInfo;
    }
}
