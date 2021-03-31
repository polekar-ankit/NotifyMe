package com.gipl.swayam.data.model.api.lib.utility;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//CHECKINOUTYPE
public class CheckInType {
    @SerializedName("BIT_BYSELF")
    @Expose
    private final int bitBySelf = 1;

    @SerializedName("BIT_MANUAL")
    @Expose
    private final int bitManual = 2;

    public int getBitBySelf() {
        return bitBySelf;
    }

    public int getBitManual() {
        return bitManual;
    }
}