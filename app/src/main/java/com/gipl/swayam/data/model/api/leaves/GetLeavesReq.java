package com.gipl.swayam.data.model.api.leaves;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetLeavesReq {
    @SerializedName("suidSession")
    @Expose
    private String suidSession;
    @SerializedName("sTag")
    @Expose
    private String tag;
    @SerializedName("isPagination")
    @Expose
    private boolean isPagination;

    @SerializedName("jStart")
    @Expose
    private int jStart;

    @SerializedName("jCount")
    @Expose
    private int jCount;


    public void setPagination(boolean pagination) {
        isPagination = pagination;
    }

    public void setjStart(int jStart) {
        this.jStart = jStart;
    }

    public void setjCount(int jCount) {
        this.jCount = jCount;
    }


    public void setSuidSession(String suidSession) {
        this.suidSession = suidSession;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
