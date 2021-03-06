package com.gipl.notifyme.data.model.api.sendotp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendOTPReq {
    @SerializedName("sEmpCode")
    @Expose
    private String empCode;

    @SerializedName("jOTPType")
    @Expose
    private int otpType = 1;

    @SerializedName("sTag")
    @Expose
    private String tag;

    public SendOTPReq(String empCode, String tag) {
        this.empCode = empCode;
        this.tag = tag;
    }
}
