package com.gipl.notifyme.data.model.api.lib.utility;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeaveFor {
    @SerializedName("BIT_FULLDAY")
    @Expose
    private int bitFullDay = 1;
    @SerializedName("BIT_HALFDAY")
    @Expose
    private int bitHalfDay = 2;

    public int getBitFullDay() {
        return bitFullDay;
    }

    public int getBitHalfDay() {
        return bitHalfDay;
    }
}
