package com.gipl.swayam.data.model.api.userimg;

import com.gipl.swayam.data.model.api.BaseReq;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserProfileImgReq extends BaseReq {
    @SerializedName("sEmpCode")
    @Expose
    private String sEmpCode;
    @SerializedName("sProfileBase64")
    @Expose
    private String sProfileBase64;

    public void setsEmpCode(String sEmpCode) {
        this.sEmpCode = sEmpCode;
    }

    public void setsProfileBase64(String sProfileBase64) {
        this.sProfileBase64 = sProfileBase64;
    }
}
