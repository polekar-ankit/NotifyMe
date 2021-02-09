package com.gipl.notifyme.data.local.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.gipl.notifyme.BuildConfig;
import com.gipl.notifyme.data.model.db.TNotification;
import com.gipl.notifyme.data.model.db.TReason;

@Database(entities = {TNotification.class, TReason.class}, version = BuildConfig.DB_VSERSION, exportSchema = false)
public abstract class NotifyMeDatabase extends RoomDatabase {

    public abstract NotificationCacheDao notificationCacheDao();

    public abstract ReasonCacheDao reasonCacheDao();
}
