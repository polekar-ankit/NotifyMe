
package com.gipl.notifyme.data.model.api.lib.utility;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeaveStatus {

    @SerializedName("BIT_PENDING")
    @Expose
    private Integer bitPending;
    @SerializedName("BIT_APPROVED")
    @Expose
    private Integer bitApproved;
    @SerializedName("BIT_REJECTED")
    @Expose
    private Integer bitRejected;
    @SerializedName("BIT_CANCELLED")
    @Expose
    private Integer bitCancelled;

    public Integer getBitPending() {
        return bitPending;
    }

    public Integer getBitApproved() {
        return bitApproved;
    }

    public Integer getBitRejected() {
        return bitRejected;
    }

    public Integer getBitCancelled() {
        return bitCancelled;
    }
}
