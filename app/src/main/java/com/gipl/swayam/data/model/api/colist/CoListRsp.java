
package com.gipl.swayam.data.model.api.colist;

import java.util.List;

import com.gipl.swayam.data.model.api.BaseRsp;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CoListRsp extends BaseRsp {
    @SerializedName("liCOR")
    @Expose
    private List<CO> CO = null;


    public List<CO> getCO() {
        return CO;
    }

    public void setCO(List<CO> CO) {
        this.CO = CO;
    }

}
