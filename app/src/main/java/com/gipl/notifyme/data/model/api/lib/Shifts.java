package com.gipl.notifyme.data.model.api.lib;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Shifts {
    @SerializedName("suidShift")
    @Expose
    private String suidShift;

    @SerializedName("sShiftName")
    @Expose
    private String shiftName;

    @SerializedName("sShiftCode")
    @Expose
    private String shiftCode;

    @SerializedName("sShiftFrom")
    @Expose
    private String shiftFrom;

    @SerializedName("shiftTo")
    @Expose
    private String shiftTo;

    @SerializedName("sWorkingHours")
    @Expose
    private String workingHours;

    @Override
    public String toString() {
        return shiftName;
    }

    public String getSuidShift() {
        return suidShift;
    }

    public String getShiftName() {
        return shiftName;
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public String getShiftFrom() {
        return shiftFrom;
    }

    public String getShiftTo() {
        return shiftTo;
    }

    public String getWorkingHours() {
        return workingHours;
    }
}
