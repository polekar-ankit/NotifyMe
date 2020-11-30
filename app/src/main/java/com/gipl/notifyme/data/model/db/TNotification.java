package com.gipl.notifyme.data.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.gipl.notifyme.data.model.api.notification.Notification;
import com.gipl.notifyme.uility.TimeUtility;

import java.text.ParseException;

@Entity(tableName = "TNotificationCache")
public class TNotification {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "j")
    private int j;
    @ColumnInfo(name = "forGrp")
    private
    String forGroup;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "message")
    private String message;
    @ColumnInfo(name = "notificationDate")
    private String notificationDate;
    @ColumnInfo(name = "linkType")
    private String linkType;
    @ColumnInfo(name = "link")
    private String link;
    @ColumnInfo(name = "cacheDate")
    private String cacheDate;

    public TNotification() {
    }

    public TNotification(Notification notification, String cacheDate) throws ParseException {
        this.forGroup = notification.getForGroup();
        this.title = notification.getTitle();
        this.message = notification.getMessage();
        this.notificationDate = TimeUtility.convertTimeToDb(notification.getNotificationDate());
        this.linkType = notification.getLinkType();
        this.link = notification.getLink();
        this.cacheDate = cacheDate;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public String getCacheDate() {
        return cacheDate;
    }

    public void setCacheDate(String cacheDate) {
        this.cacheDate = cacheDate;
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
