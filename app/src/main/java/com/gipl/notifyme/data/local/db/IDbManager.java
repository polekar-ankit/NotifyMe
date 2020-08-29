package com.gipl.notifyme.data.local.db;

import androidx.lifecycle.LiveData;

import com.gipl.notifyme.data.model.api.notification.Notification;
import com.gipl.notifyme.data.model.db.TNotification;

import java.util.List;

public interface IDbManager {
    //Notification cache
    long insertNotification(TNotification tNotification);

    LiveData<List<Notification>> getNotificationList();

    int getTotalNotificationCacheRm();
    //**********************
}
