package com.gipl.swayam.data.model.api.userimg;

import com.gipl.swayam.data.model.api.BaseRsp;
import com.gipl.swayam.data.model.api.sendotp.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserProfileImgRsp extends BaseRsp {
    @SerializedName("objUser")
    @Expose
    private User user;

    public User getUser() {
        return user;
    }
}
