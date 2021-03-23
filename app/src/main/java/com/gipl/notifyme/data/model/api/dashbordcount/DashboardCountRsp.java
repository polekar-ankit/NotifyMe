package com.gipl.notifyme.data.model.api.dashbordcount;

import com.gipl.notifyme.data.model.api.BaseRsp;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardCountRsp extends BaseRsp {
    @SerializedName("objMobileDashboardCount")
    @Expose
    private Counts counts;

    public Counts getCounts() {
        return counts;
    }
}
