package com.gipl.swayam.data.model.api.notification;

import androidx.room.Ignore;

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

    @SerializedName("sLinkType")
    @Expose
    private String linkType;
    @SerializedName("sLink")
    @Expose
    private String link;

    @Expose(deserialize = false, serialize = false)
    @Ignore
    private int resId;

    @Expose(deserialize = false, serialize = false)
    @Ignore
    private String displayDate;



    public String getDisplayDate() {
        return displayDate;
    }

    public void setDisplayDate(String displayDate) {
        this.displayDate = displayDate;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

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

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
