package com.gipl.notifyme.domain;

import com.gipl.notifyme.data.DataManager;

public class UseCase {
    protected DataManager dataManager;

    public UseCase(DataManager dataManager) {
        this.dataManager = dataManager;
    }
}
