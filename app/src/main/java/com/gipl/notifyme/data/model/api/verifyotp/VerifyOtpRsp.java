package com.gipl.notifyme.data.model.api.verifyotp;

import com.gipl.notifyme.data.model.api.BaseRsp;
import com.gipl.notifyme.data.model.api.sendotp.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyOtpRsp extends BaseRsp {
    @SerializedName("sSession")
    @Expose
    private String session;

    @SerializedName("oUser")
    @Expose
    private User user;

    public String getSession() {
        return session;
    }

    public User getUser() {
        return user;
    }
}
