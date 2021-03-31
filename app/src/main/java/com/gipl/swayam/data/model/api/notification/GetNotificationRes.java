package com.gipl.swayam.data.model.api.notification;

import com.gipl.swayam.data.model.api.BaseRsp;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetNotificationRes extends BaseRsp {
    @SerializedName("isPagination")
    @Expose
    private boolean pagination;

    @SerializedName("jAvailableRecords")
    @Expose
    private int availableRecords;

    @SerializedName("jStart")
    @Expose
    private int start;

    @SerializedName("liNotification")
    @Expose
    private ArrayList<Notification> notifications;


    public boolean isPagination() {
        return pagination;
    }

    public void setPagination(boolean pagination) {
        this.pagination = pagination;
    }

    public int getAvailableRecords() {
        return availableRecords;
    }

    public void setAvailableRecords(int availableRecords) {
        this.availableRecords = availableRecords;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<Notification> notifications) {
        this.notifications = notifications;
    }
}
