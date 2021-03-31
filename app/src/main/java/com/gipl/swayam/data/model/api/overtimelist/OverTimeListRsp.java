
package com.gipl.swayam.data.model.api.overtimelist;

import java.util.List;

import com.gipl.swayam.data.model.api.BaseRsp;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OverTimeListRsp extends BaseRsp {

    @SerializedName("isPagination")
    @Expose
    private Boolean isPagination;
    @SerializedName("jAvailableRecords")
    @Expose
    private Integer jAvailableRecords;
    @SerializedName("jStart")
    @Expose
    private Integer jStart;
    @SerializedName("liOT")
    @Expose
    private List<OT> OT = null;


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

    public Integer getJStart() {
        return jStart;
    }

    public void setJStart(Integer jStart) {
        this.jStart = jStart;
    }

    public List<OT> getOT() {
        return OT;
    }

    public void setOT(List<OT> OT) {
        this.OT = OT;
    }


}
