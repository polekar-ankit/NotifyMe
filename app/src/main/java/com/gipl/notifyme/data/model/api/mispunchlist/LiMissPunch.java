
package com.gipl.notifyme.data.model.api.mispunchlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LiMissPunch {

    @SerializedName("suidEmployee")
    @Expose
    private String suidEmployee;
    @SerializedName("suidUserAplicant")
    @Expose
    private String suidUserAplicant;
    @SerializedName("sEmpCode")
    @Expose
    private String sEmpCode;
    @SerializedName("sName")
    @Expose
    private String sName;
    @SerializedName("sLocation")
    @Expose
    private String sLocation;
    @SerializedName("sDepartment")
    @Expose
    private String sDepartment;
    @SerializedName("sDesignation")
    @Expose
    private String sDesignation;
    @SerializedName("sCompany")
    @Expose
    private String sCompany;
    @SerializedName("sShift")
    @Expose
    private String sShift;
    @SerializedName("suidShift")
    @Expose
    private String suidShift;
    @SerializedName("sUserApplicantName")
    @Expose
    private String sUserApplicantName;
    @SerializedName("suidEmployeeType")
    @Expose
    private String suidEmployeeType;
    @SerializedName("sEmployeeType")
    @Expose
    private String sEmployeeType;
    @SerializedName("suidMissPunch")
    @Expose
    private String suidMissPunch;
    @SerializedName("sInTime")
    @Expose
    private String inTime;
    @SerializedName("sOutTime")
    @Expose
    private String outTime;
    @SerializedName("sDtMissPunch")
    @Expose
    private String dtMissPunch;
    @SerializedName("sReason")
    @Expose
    private String sReason;
    @SerializedName("jStatus")
    @Expose
    private Integer jStatus;

    public void setStatusDis(String statusDis) {
        this.statusDis = statusDis;
    }

    @Expose(deserialize = false, serialize = false)
    private String statusDis;
    @Expose(serialize = false, deserialize = false)
    private int color;

    public int getStatus() {
        return jStatus;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getStatusDis() {
        return statusDis;
    }

    public String getInTime() {
        return inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public String getDtMissPunch() {
        return dtMissPunch;
    }


}
