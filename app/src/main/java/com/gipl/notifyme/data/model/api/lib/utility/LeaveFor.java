package com.gipl.notifyme.data.model.api.lib.utility;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeaveFor {
    @SerializedName("BIT_FULLDAY")
    @Expose
    private final int bitFullDay = 1;
    @SerializedName("BIT_FIRSTHALFDAY")
    @Expose
    private final int bitFirstHalfDay = 2;
    @SerializedName("BIT_SECONDHALFDAY")
    @Expose
    private final int bitSecondHalfDay = 4;

    public int getBitFullDay() {
        return bitFullDay;
    }

    public int getBitFirstHalfDay() {
        return bitFirstHalfDay;
    }

    public int getBitSecondHalfDay() {
        return bitSecondHalfDay;
    }
}
