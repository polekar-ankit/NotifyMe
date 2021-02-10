package com.gipl.notifyme.data.local.db;

import androidx.lifecycle.LiveData;
import androidx.room.Query;

import com.gipl.notifyme.data.model.api.notification.Notification;
import com.gipl.notifyme.data.model.db.TNotification;
import com.gipl.notifyme.data.model.db.TReason;
import com.gipl.notifyme.ui.model.Reason;

import java.util.List;

public interface IDbManager {
    //Notification cache
    long insertNotification(TNotification tNotification);

    LiveData<List<Notification>> getNotificationList();

    int getTotalNotificationCacheRm();

    int clearNotificationCache();
    //**********************

    //REASON CACHE
    long insertReason(TReason tReason);

    LiveData<List<Reason>> getReasonList(String type);

    int clearReason(String type);

    int clearAllReason();
    //*************************
}
