
package com.gipl.swayam.data.model.api.lib.utility;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatusType {

    @SerializedName("BIT_ACTIVE")
    @Expose
    private Integer bITACTIVE;
    @SerializedName("BIT_DELETED")
    @Expose
    private Integer bITDELETED;
    @SerializedName("BIT_CONSUMED")
    @Expose
    private Integer bITCONSUMED;
    @SerializedName("BIT_OBSOLETE")
    @Expose
    private Integer bITOBSOLETE;
    @SerializedName("BIT_DEACTIVE")
    @Expose
    private Integer bITDEACTIVE;
    @SerializedName("BIT_CANCELLED")
    @Expose
    private Integer bITCANCELLED;
    @SerializedName("BIT_VERIFIED")
    @Expose
    private Integer bITVERIFIED;

    public Integer getBITACTIVE() {
        return bITACTIVE;
    }

    public void setBITACTIVE(Integer bITACTIVE) {
        this.bITACTIVE = bITACTIVE;
    }

    public Integer getBITDELETED() {
        return bITDELETED;
    }

    public void setBITDELETED(Integer bITDELETED) {
        this.bITDELETED = bITDELETED;
    }

    public Integer getBITCONSUMED() {
        return bITCONSUMED;
    }

    public void setBITCONSUMED(Integer bITCONSUMED) {
        this.bITCONSUMED = bITCONSUMED;
    }

    public Integer getBITOBSOLETE() {
        return bITOBSOLETE;
    }

    public void setBITOBSOLETE(Integer bITOBSOLETE) {
        this.bITOBSOLETE = bITOBSOLETE;
    }

    public Integer getBITDEACTIVE() {
        return bITDEACTIVE;
    }

    public void setBITDEACTIVE(Integer bITDEACTIVE) {
        this.bITDEACTIVE = bITDEACTIVE;
    }

    public Integer getBITCANCELLED() {
        return bITCANCELLED;
    }

    public void setBITCANCELLED(Integer bITCANCELLED) {
        this.bITCANCELLED = bITCANCELLED;
    }

    public Integer getBITVERIFIED() {
        return bITVERIFIED;
    }

    public void setBITVERIFIED(Integer bITVERIFIED) {
        this.bITVERIFIED = bITVERIFIED;
    }

}
