package com.gipl.notifyme.data.model.api.lib;

import com.gipl.notifyme.data.model.api.BaseRsp;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetLibRes extends BaseRsp {

    @SerializedName("liShifts")
    @Expose
    private List<Shifts> shiftsList;
    @SerializedName("sJson")
    @Expose
    private String sJson;

    public String getsJson() {
        return sJson;
    }



    public List<Shifts> getShiftsList() {
        return shiftsList;
    }
}
