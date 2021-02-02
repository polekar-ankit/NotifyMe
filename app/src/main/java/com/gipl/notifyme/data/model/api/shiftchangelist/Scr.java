
package com.gipl.notifyme.data.model.api.shiftchangelist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Shift change request object
 */
public class Scr {

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
    @SerializedName("suidShiftFrom")
    @Expose
    private String suidShiftFrom;
    @SerializedName("sShiftFrom")
    @Expose
    private String shiftFrom;
    @SerializedName("suidShiftTo")
    @Expose
    private String suidShiftTo;
    @SerializedName("sShiftTo")
    @Expose
    private String shiftTo;
    @SerializedName("sUserApplicantName")
    @Expose
    private String sUserApplicantName;
    @SerializedName("suidEmployeeType")
    @Expose
    private String suidEmployeeType;
    @SerializedName("sEmployeeType")
    @Expose
    private String sEmployeeType;
    @SerializedName("suidSCR")
    @Expose
    private String suidSCR;
    @SerializedName("sDtShiftFrom")
    @Expose
    private String dtShiftFrom;
    @SerializedName("sDtShiftTo")
    @Expose
    private String dtShiftTo;
    @SerializedName("sReason")
    @Expose
    private String reason;
    @SerializedName("jStatus")
    @Expose
    private Integer jStatus;
    @Expose(serialize = false, deserialize = false)
    private int statusColor;
    @Expose(serialize = false, deserialize = false)
    private String statusDis;

    public int getStatusColor() {
        return statusColor;
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

    public String getSuidShiftFrom() {
        return suidShiftFrom;
    }

    public void setSuidShiftFrom(String suidShiftFrom) {
        this.suidShiftFrom = suidShiftFrom;
    }

    public String getShiftFrom() {
        return shiftFrom;
    }

    public void setShiftFrom(String sShiftFrom) {
        this.shiftFrom = sShiftFrom;
    }

    public String getSuidShiftTo() {
        return suidShiftTo;
    }

    public void setSuidShiftTo(String suidShiftTo) {
        this.suidShiftTo = suidShiftTo;
    }

    public String getShiftTo() {
        return shiftTo;
    }

    public void setSShiftTo(String sShiftTo) {
        this.shiftTo = sShiftTo;
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

    public String getSuidSCR() {
        return suidSCR;
    }

    public void setSuidSCR(String suidSCR) {
        this.suidSCR = suidSCR;
    }

    public String getDtShiftFrom() {
        return dtShiftFrom;
    }

    public void setDtShiftFrom(String sDtShiftFrom) {
        this.dtShiftFrom = sDtShiftFrom;
    }

    public String getDtShiftTo() {
        return dtShiftTo;
    }

    public void setDtShiftTo(String sDtShiftTo) {
        this.dtShiftTo = sDtShiftTo;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String sReason) {
        this.reason = sReason;
    }

    public int getStatus() {
        return jStatus;
    }

    public void setJStatus(Integer jStatus) {
        this.jStatus = jStatus;
    }

    public void setColor(int parseColor) {
        this.statusColor = parseColor;
    }
}
