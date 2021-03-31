package com.gipl.swayam.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseRsp {
    @SerializedName("apiError")
    @Expose
    private ApiError apiError;
    @SerializedName("sTag")
    @Expose
    private String tag;

    public ApiError getApiError() {
        return apiError;
    }

    public String getTag() {
        return tag;
    }
}
