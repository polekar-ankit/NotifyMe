package com.gipl.notifyme.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BaseListReq extends BaseReq {
    @SerializedName("isPagination")
    @Expose
    private boolean isPagination;

    @SerializedName("jStart")
    @Expose
    private int jStart;

    @SerializedName("jCount")
    @Expose
    private int jCount;

    @SerializedName("sFilter")
    @Expose
    private String filter;

    public void setPagination(boolean pagination) {
        isPagination = pagination;
    }

    public void setjStart(int jStart) {
        this.jStart = jStart;
    }

    public void setjCount(int jCount) {
        this.jCount = jCount;
    }

    public void setFilter(String empNoOrEmpName) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        JSONObject emp = new JSONObject();
        emp.put("sKey", "EmpNoOrEmpName");
        emp.put("sValue", empNoOrEmpName);
        jsonArray.put(emp);
        filter = jsonArray.toString();
    }
}
