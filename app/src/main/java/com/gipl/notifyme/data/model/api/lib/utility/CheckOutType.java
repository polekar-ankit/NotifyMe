package com.gipl.notifyme.data.model.api.lib.utility;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckOutType {
    @SerializedName("BIT_LUNCH")
    @Expose
    private final int bitLunch = 1;

    @SerializedName("BIT_DAYEND")
    @Expose
    private final int bitDayEnd = 2;

    @SerializedName("BIT_OFFICIALOUT")
    @Expose
    private final int bitOfficialOut = 4;

    public int getBitLunch() {
        return bitLunch;
    }

    public int getBitDayEnd() {
        return bitDayEnd;
    }

    public int getBitOfficialOut() {
        return bitOfficialOut;
    }
}
