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

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setSuid(int suid) {
        this.suid = suid;
    }

    public static class Type {
        public static String CO_REASON = "TCoReason";
        public static String MISS_PUNCH_REASON = "TMissPunchReason";
        public static String OT_REASON = "TOtReason";
        public static String SHIFT_CHANGE_REASON = "TShiftChangeReason";
        public static String LEAVE_REASON = "TLeaveReason";
    }
}
