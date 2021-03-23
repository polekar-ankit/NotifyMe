package com.gipl.notifyme.data.model.api.dashbordcount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Counts {
    @SerializedName("jMissPunchCount")
    @Expose
    private int missPunchCount;

    public int getMissPunchCount() {
        return missPunchCount;
    }
}
