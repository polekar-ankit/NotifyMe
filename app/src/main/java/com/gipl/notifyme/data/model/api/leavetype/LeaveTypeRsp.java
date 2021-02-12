package com.gipl.notifyme.data.model.api.leavetype;

import com.gipl.notifyme.data.model.api.BaseRsp;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LeaveTypeRsp extends BaseRsp {
    @SerializedName("liLeaveApprovals")
    @Expose
    private final ArrayList<LeaveApproval> liLeaveApprovals = null;

    public ArrayList<LeaveApproval> getLiLeaveApprovals() {
        return liLeaveApprovals;
    }
}
