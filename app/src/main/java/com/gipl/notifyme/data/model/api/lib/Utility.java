package com.gipl.notifyme.data.model.api.lib;

import com.gipl.notifyme.data.model.api.lib.utility.CheckInType;
import com.gipl.notifyme.data.model.api.lib.utility.CheckOutType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Utility {
    @SerializedName("CHECKOUTTYPE")
    @Expose
    private CheckOutType checkOutType;

    @SerializedName("CHECKINOUTYPE")
    @Expose
    private CheckInType checkInType;

    public void setCheckOutType(CheckOutType checkOutType) {
        this.checkOutType = checkOutType;
    }

    public void setCheckInType(CheckInType checkInType) {
        this.checkInType = checkInType;
    }

    public CheckOutType getCheckOutType() {
        return checkOutType;
    }

    public CheckInType getCheckInType() {
        return checkInType;
    }
}
