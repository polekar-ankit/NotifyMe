
package com.gipl.swayam.data.model.api.colist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CO {

    @SerializedName("suidEmployee")
    @Expose
    private String suidEmployee;
    @SerializedName("suidUserAplicant")
    @Expose
    private String suidUserAplicant;
    @SerializedName("sEmpCode")
    @Expose
    private String empCode;
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
    @SerializedName("sUserApplicantName")
    @Expose
    private String sUserApplicantName;
    @SerializedName("suidEmployeeType")
    @Expose
    private String suidEmployeeType;
    @SerializedName("sEmployeeType")
    @Expose
    private String sEmployeeType;
    @SerializedName("suidCO")
    @Expose
    private String suidCO;
    @SerializedName("rHours")
    @Expose
    private Double rHours;
    @SerializedName("jCOFor")
    @Expose
    private Integer jCOFor;
    @Expose
    private String lblCOFor;
    @SerializedName("sDtCO")
    @Expose
    private String dtCO;
    @SerializedName("sReason")
    @Expose
    private String sReason;
    @SerializedName("jStatus")
    @Expose
    private int status;
    @Expose(deserialize = false, serialize = false)
    private String statusDis;
    @Expose(serialize = false, deserialize = false)
    private int color;

    public String getLblCOFor() {
        return lblCOFor;
    }

    public void setLblCOFor(String lblCOFor) {
        this.lblCOFor = lblCOFor;
    }

    public String getStatusDis() {
        return statusDis;
    }

    public void setStatusDis(String statusDis) {
        this.statusDis = statusDis;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
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
        return empCode;
    }

    public void setSEmpCode(String sEmpCode) {
        this.empCode = sEmpCode;
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

    public String getSuidCO() {
        return suidCO;
    }

    public void setSuidCO(String suidCO) {
        this.suidCO = suidCO;
    }

    public Double getRHours() {
        return rHours;
    }

    public void setRHours(Double rHours) {
        this.rHours = rHours;
    }

    public Integer getCOFor() {
        return jCOFor;
    }

    public void setCOFor(Integer jCOFor) {
        this.jCOFor = jCOFor;
    }

    public String getDtCO() {
        return dtCO;
    }

    public void setDtCO(String sDtCO) {
        this.dtCO = sDtCO;
    }

    public String getReason() {
        return sReason;
    }

    public void setReason(String sReason) {
        this.sReason = sReason;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(Integer jStatus) {
        this.status = jStatus;
    }

}
