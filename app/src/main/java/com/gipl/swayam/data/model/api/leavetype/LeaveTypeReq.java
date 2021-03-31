package com.gipl.swayam.data.model.api.leavetype;

import com.gipl.swayam.data.model.api.BaseReq;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeaveTypeReq extends BaseReq {
    @SerializedName("isPagination")
    @Expose
    private boolean isPagination;

    public void setPagination(boolean pagination) {
        isPagination = pagination;
    }
}