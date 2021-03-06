package com.gipl.notifyme.data.model.api.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetNotificationsReq {
    @SerializedName("sEmpCode")
    @Expose
    private String empCode;

    @SerializedName("suidSession ")
    @Expose
    private String suidSession = "";

    @SerializedName("isPagination")
    @Expose
    private boolean pagination = false;

    @SerializedName("sFilter")
    @Expose
    private String filter = "";;

    @SerializedName("jStart")
    @Expose
    private int start = 0;

    @SerializedName("jCount")
    @Expose
    private int count = 10;

    public GetNotificationsReq() {
    }

    public String getSuidSession() {
        return suidSession;
    }

    public void setSuidSession(String suidSession) {
        this.suidSession = suidSession;
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
