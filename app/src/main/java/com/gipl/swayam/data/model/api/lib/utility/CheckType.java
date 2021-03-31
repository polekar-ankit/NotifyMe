package com.gipl.swayam.data.model.api.lib.utility;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckType {
    @SerializedName("BIT_CHECKIN")
    @Expose
    private final int BitCheckIn = 1;

    @SerializedName("BIT_CHECKOUT")
    @Expose
    private final int BitCheckOut = 2;

    public int getBitCheckIn() {
        return BitCheckIn;
    }

    public int getBitCheckOut() {
        return BitCheckOut;
    }
}
