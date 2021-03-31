package com.gipl.swayam.data.model.api.usershift;

import com.gipl.swayam.data.model.api.BaseRsp;
import com.gipl.swayam.data.model.api.lib.Shifts;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserShiftRsp extends BaseRsp {
    public List<Shifts> getShiftsList() {
        return shiftsList;
    }

    @SerializedName("liShifts")
    @Expose
    private List<Shifts> shiftsList;
}
