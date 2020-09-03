package com.gipl.notifyme.data.model.api.lib;

import com.gipl.notifyme.data.model.api.ApiError;
import com.gipl.notifyme.data.model.api.BaseRsp;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetLibRes extends BaseRsp {
    @SerializedName("sJson")
    @Expose
    private String jsonString;

    @SerializedName("liShifts")
    @Expose
    private List<Shifts> shiftsList;

    public String getJsonString() {
        return jsonString;
    }


    public List<Shifts> getShiftsList() {
        return shiftsList;
    }
}
