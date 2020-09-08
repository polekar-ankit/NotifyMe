package com.gipl.notifyme.ui.model;

public class LeaveFor {
    private String name;
    private int suid;

    public LeaveFor(String name, int suid) {
        this.name = name;
        this.suid = suid;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public int getSuid() {
        return suid;
    }
}
