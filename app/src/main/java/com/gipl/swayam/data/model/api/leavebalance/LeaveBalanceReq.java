package com.gipl.swayam.data.model.api.leavebalance;

import com.gipl.swayam.data.model.api.BaseListReq;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeaveBalanceReq extends BaseListReq {
    @SerializedName("suidUser")
    @Expose
    private String suidUser;

    public void setSuidUser(String suidUser) {
        this.suidUser = suidUser;
    }
}
