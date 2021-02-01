
package com.gipl.notifyme.data.model.api.mispunchlist;

import java.util.List;

import com.gipl.notifyme.data.model.api.BaseRsp;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MissPunchListRsp extends BaseRsp {

    @SerializedName("isPagination")
    @Expose
    private Boolean isPagination;
    @SerializedName("jAvailableRecords")
    @Expose
    private Integer jAvailableRecords;

    @SerializedName("liMissPunch")
    @Expose
    private List<LiMissPunch> liMissPunch = null;


    public Boolean getIsPagination() {
        return isPagination;
    }

    public void setIsPagination(Boolean isPagination) {
        this.isPagination = isPagination;
    }

    public Integer getJAvailableRecords() {
        return jAvailableRecords;
    }

    public void setJAvailableRecords(Integer jAvailableRecords) {
        this.jAvailableRecords = jAvailableRecords;
    }


    public List<LiMissPunch> getLiMissPunch() {
        return liMissPunch;
    }

    public void setLiMissPunch(List<LiMissPunch> liMissPunch) {
        this.liMissPunch = liMissPunch;
    }


}
