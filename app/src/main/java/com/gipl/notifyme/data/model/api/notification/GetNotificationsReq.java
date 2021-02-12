package com.gipl.notifyme.data.model.api.notification;

import com.gipl.notifyme.data.model.api.BaseReq;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetNotificationsReq  {
    @SerializedName("jClientType")
    @Expose
    private final int clientType = 2;
    @SerializedName("sEmpCode")
    @Expose
    private String empCode;

    @SerializedName("isPagination")
    @Expose
    private boolean pagination = false;

    @SerializedName("sFilter")
    @Expose
    private String filter = "";

    @SerializedName("jStart")
    @Expose
    private int start = 0;

    @SerializedName("jCount")
    @Expose
    private int count = 10;

    public GetNotificationsReq() {
    }
    

    public boolean getPagination() {
        return pagination;
    }

    public void setPagination(boolean pagination) {
        this.pagination = pagination;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }
}
