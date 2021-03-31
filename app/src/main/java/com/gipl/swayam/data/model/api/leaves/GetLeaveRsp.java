package com.gipl.swayam.data.model.api.leaves;

import com.gipl.swayam.data.model.api.BaseRsp;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetLeaveRsp extends BaseRsp {
    @SerializedName("liLeaveRequests")
    @Expose
    private ArrayList<LeaveRequest> leaveRequestArrayList;

    public ArrayList<LeaveRequest> getLeaveRequestArrayList() {
        return leaveRequestArrayList;
    }
}
