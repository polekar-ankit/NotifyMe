
package com.gipl.notifyme.data.model.api.lib;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SJson {

    @SerializedName("Utility")
    @Expose
    private Utility utility;

    public Utility getUtility() {
        return utility;
    }

    public void setUtility(Utility utility) {
        this.utility = utility;
    }

}
