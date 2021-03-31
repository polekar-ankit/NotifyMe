package com.gipl.swayam.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiError {
    @SerializedName("jErrorVal")
    @Expose
    private int errorVal;

    @SerializedName("sErrorMessage")
    @Expose
    private String errorMessage;

    public int getErrorVal() {
        return errorVal;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public static class ERROR_CODE {
        public static int OK = 0;
    }
}
