package com.gipl.swayam.data.local.db;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.gipl.swayam.BuildConfig;
import com.gipl.swayam.data.model.db.TNotification;
import com.gipl.swayam.data.model.db.TReason;

@Database(entities = {TNotification.class, TReason.class}, version = BuildConfig.DB_VSERSION, exportSchema = false)
public abstract class NotifyMeDatabase extends RoomDatabase {

    public abstract NotificationCacheDao notificationCacheDao();

    public abstract ReasonCacheDao reasonCacheDao();

}
