package com.gipl.notifyme.data.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "TReasonCache")
public class TReason {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "j")
    private int j;

    @ColumnInfo(name = "reason")
    private String reason;

    @ColumnInfo(name = "suid")
    private int suid;

    @ColumnInfo(name = "type")
    private String type;

    public TReason() {
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getSuid() {
        return suid;
    }

    public void setSuid(int suid) {
        this.suid = suid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
