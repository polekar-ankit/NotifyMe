package com.gipl.notifyme.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseReq {
    @SerializedName("jClientType")
    @Expose
    private int clientType = 2;
    @SerializedName("suidSession")
    @Expose
    private String suidSession;
    @SerializedName("sTag")
    @Expose
    private String tag;

    public void setClientType(int clientType) {
        this.clientType = clientType;
    }

    public void setSuidSession(String suidSession) {
        this.suidSession = suidSession;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
