package com.gipl.notifyme.data.model.api.verifyotp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyOtpReq {
    @SerializedName("jClientType")
    @Expose
    private final int clientType = 2;

    @SerializedName("sUserName")
    @Expose
    private final String empCode;

    @SerializedName("sPassword")
    @Expose
    private final String password;

    @SerializedName("sOTP")
    @Expose
    private final String otp;

    @SerializedName("sFirebaseDeviceToken")
    @Expose
    private final String firebaseDeviceToken;

    @SerializedName("sTag")
    @Expose
    private final String tag;

    public VerifyOtpReq(String empCode, String otp, String firebaseDeviceToken, String tag) {
        this.empCode = empCode;
        this.otp = otp;
        this.firebaseDeviceToken = firebaseDeviceToken;
        this.tag = tag;
        this.password = "";
    }
}
