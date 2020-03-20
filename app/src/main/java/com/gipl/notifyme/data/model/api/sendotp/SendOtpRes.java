package com.gipl.notifyme.data.model.api.sendotp;

import com.gipl.notifyme.data.model.api.BaseRsp;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendOtpRes extends BaseRsp {
    @SerializedName("oUser")
    @Expose
    private User user;
    @SerializedName("sMessage")
    @Expose
    private String message;

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }
}
