package com.gipl.notifyme.data.local.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.gipl.notifyme.data.model.api.notification.Notification;
import com.gipl.notifyme.data.model.db.TNotification;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface NotificationCacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertNotification(TNotification tNotification);

    @Query("SELECT forGrp as forGroup,title as title,message as message,link as link,linkType as linkType,notificationDate as notificationDate" +
            " FROM TNotificationCache")
    LiveData<List<Notification>>getNotificationList();

    @Query("SELECT COUNT(j) from TNotificationCache")
    int getTotalNotificationCache();

}