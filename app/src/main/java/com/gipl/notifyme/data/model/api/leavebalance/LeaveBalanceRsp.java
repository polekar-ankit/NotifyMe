package com.gipl.notifyme.data.model.api.leavebalance;

import com.gipl.notifyme.data.model.api.BaseRsp;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LeaveBalanceRsp extends BaseRsp {
    @SerializedName("liLeaveBalance")
    @Expose
    ArrayList<LeaveBalance> balanceArrayList;

    public ArrayList<LeaveBalance> getBalanceArrayList() {
        return balanceArrayList;
    }
}
