
package com.gipl.notifyme.data.model.api.overtimelist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OT {

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
    @SerializedName("sPlant")
    @Expose
    private String sPlant;
    @SerializedName("suidPlant")
    @Expose
    private String suidPlant;
    @SerializedName("sUserApplicantName")
    @Expose
    private String sUserApplicantName;
    @SerializedName("suidEmployeeType")
    @Expose
    private String suidEmployeeType;
    @SerializedName("sEmployeeType")
    @Expose
    private String sEmployeeType;
    @SerializedName("suidOverTime")
    @Expose
    private String suidOverTime;
    @SerializedName("rOTHrs")
    @Expose
    private Double rOTHrs;

    @SerializedName("rIntesiveHrs")
    @Expose
    private Double rIntesiveHrs;
    @SerializedName("sDtOverTime")
    @Expose
    private String sDtOverTime;
    @SerializedName("sReason")
    @Expose
    private String sReason;
    @SerializedName("jStatus")
    @Expose
    private int jStatus;
    @Expose(deserialize = false, serialize = false)
    private String statusDis;
    @Expose(serialize = false, deserialize = false)
    private int color;
    @Expose(serialize = false, deserialize = false)
    private String disOT;

    public String getDisOT() {
        return disOT;
    }

    public void setDisOT(String disOT) {
        this.disOT = disOT;
    }

    public Double getrOTHrs() {
        return rOTHrs;
    }

    public String getsDtOverTime() {
        return sDtOverTime;
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

    public void setStatusDis(String statusDis) {
        this.statusDis = statusDis;
    }

    public String getSuidEmployee() {
        return suidEmployee;
    }

    public void setSuidEmployee(String suidEmployee) {
        this.suidEmployee = suidEmployee;
    }

    public String getSuidUserAplicant() {
        return suidUserAplicant;
    }

    public void setSuidUserAplicant(String suidUserAplicant) {
        this.suidUserAplicant = suidUserAplicant;
    }

    public String getSEmpCode() {
        return sEmpCode;
    }

    public void setSEmpCode(String sEmpCode) {
        this.sEmpCode = sEmpCode;
    }

    public String getSName() {
        return sName;
    }

    public void setSName(String sName) {
        this.sName = sName;
    }

    public String getSLocation() {
        return sLocation;
    }

    public void setSLocation(String sLocation) {
        this.sLocation = sLocation;
    }

    public String getSDepartment() {
        return sDepartment;
    }

    public void setSDepartment(String sDepartment) {
        this.sDepartment = sDepartment;
    }

    public String getSDesignation() {
        return sDesignation;
    }

    public void setSDesignation(String sDesignation) {
        this.sDesignation = sDesignation;
    }

    public String getSCompany() {
        return sCompany;
    }

    public void setSCompany(String sCompany) {
        this.sCompany = sCompany;
    }

    public String getSPlant() {
        return sPlant;
    }

    public void setSPlant(String sPlant) {
        this.sPlant = sPlant;
    }

    public String getSuidPlant() {
        return suidPlant;
    }

    public void setSuidPlant(String suidPlant) {
        this.suidPlant = suidPlant;
    }

    public String getSUserApplicantName() {
        return sUserApplicantName;
    }

    public void setSUserApplicantName(String sUserApplicantName) {
        this.sUserApplicantName = sUserApplicantName;
    }

    public String getSuidEmployeeType() {
        return suidEmployeeType;
    }

    public void setSuidEmployeeType(String suidEmployeeType) {
        this.suidEmployeeType = suidEmployeeType;
    }

    public String getSEmployeeType() {
        return sEmployeeType;
    }

    public void setSEmployeeType(String sEmployeeType) {
        this.sEmployeeType = sEmployeeType;
    }

    public String getSuidOverTime() {
        return suidOverTime;
    }

    public void setSuidOverTime(String suidOverTime) {
        this.suidOverTime = suidOverTime;
    }

    public Double getROTHrs() {
        return rOTHrs;
    }

    public void setROTHrs(Double rOTHrs) {
        this.rOTHrs = rOTHrs;
    }

    public Double getRIntesiveHrs() {
        return rIntesiveHrs;
    }

    public void setRIntesiveHrs(Double rIntesiveHrs) {
        this.rIntesiveHrs = rIntesiveHrs;
    }

    public String getSDtOverTime() {
        return sDtOverTime;
    }

    public void setSDtOverTime(String sDtOverTime) {
        this.sDtOverTime = sDtOverTime;
    }

    public String getReason() {
        return sReason;
    }

    public void setSReason(String sReason) {
        this.sReason = sReason;
    }

    public int getStatus() {
        return jStatus;
    }

    public void setJStatus(Integer jStatus) {
        this.jStatus = jStatus;
    }

}
