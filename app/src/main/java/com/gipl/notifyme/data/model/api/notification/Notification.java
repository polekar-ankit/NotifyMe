package com.gipl.notifyme.data.model.api.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notification {
    @SerializedName("sFor")
    @Expose
    private String forGroup;
    @SerializedName("sTitle")
    @Expose
    private String title;
    @SerializedName("sMessage")
    @Expose
    private String message;
    @SerializedName("sNotificationDate")
    @Expose
    private String notificationDate;

    public String getForGroup() {
        return forGroup;
    }

    public void setForGroup(String forGroup) {
        this.forGroup = forGroup;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(String notificationDate) {
        this.notificationDate = notificationDate;
    }
}
