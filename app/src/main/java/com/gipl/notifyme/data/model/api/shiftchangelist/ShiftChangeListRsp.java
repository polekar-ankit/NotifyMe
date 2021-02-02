
package com.gipl.notifyme.data.model.api.shiftchangelist;

import java.util.List;

import com.gipl.notifyme.data.model.api.BaseRsp;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShiftChangeListRsp extends BaseRsp {

    @SerializedName("liSCR")
    @Expose
    private List<Scr> scr = null;

    public List<Scr> getScr() {
        return scr;
    }

    public void setScr(List<Scr> scr) {
        this.scr = scr;
    }


}
