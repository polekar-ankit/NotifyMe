
package com.gipl.notifyme.data.model.api.lib.utility;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttendanceStatus {

    @SerializedName("BIT_PENDING")
    @Expose
    private Integer bITPENDING;
    @SerializedName("BIT_APPROVED")
    @Expose
    private Integer bITAPPROVED;
    @SerializedName("BIT_DISAPPROVED")
    @Expose
    private Integer bITDISAPPROVED;

    public Integer getBITPENDING() {
        return bITPENDING;
    }

    public void setBITPENDING(Integer bITPENDING) {
        this.bITPENDING = bITPENDING;
    }

    public Integer getBITAPPROVED() {
        return bITAPPROVED;
    }

    public void setBITAPPROVED(Integer bITAPPROVED) {
        this.bITAPPROVED = bITAPPROVED;
    }

    public Integer getBITDISAPPROVED() {
        return bITDISAPPROVED;
    }

    public void setBITDISAPPROVED(Integer bITDISAPPROVED) {
        this.bITDISAPPROVED = bITDISAPPROVED;
    }

}
