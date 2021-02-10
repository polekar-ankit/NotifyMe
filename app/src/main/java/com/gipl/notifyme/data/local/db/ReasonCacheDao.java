package com.gipl.notifyme.data.local.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.gipl.notifyme.data.model.db.TReason;
import com.gipl.notifyme.ui.model.Reason;

import java.util.List;

@Dao
public interface ReasonCacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertReason(TReason tReason);

    @Query("SELECT reason as reason,suid as suid FROM TReasonCache where type=:type ORDER BY suid")
    LiveData<List<Reason>> getReasonList(String type);

    @Query("DELETE FROM TReasonCache where type=:type")
    int clear(String type);

    @Query("DELETE FROM TReasonCache")
    int clearAllReason();


}
