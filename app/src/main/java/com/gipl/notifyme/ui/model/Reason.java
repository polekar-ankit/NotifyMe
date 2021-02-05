package com.gipl.notifyme.ui.model;

public class Reason {
    private String reason;
    private int suid;

    @Override
    public String toString() {
        return reason;
    }

    public Reason(String reason, int suid) {
        this.reason = reason;
        this.suid = suid;
    }

    public String getReason() {
        return reason;
    }

    public int getSuid() {
        return suid;
    }
}
