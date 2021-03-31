package com.gipl.swayam.domain;

import com.gipl.swayam.data.DataManager;

public class UseCase {
    protected DataManager dataManager;

    public UseCase(DataManager dataManager) {
        this.dataManager = dataManager;
    }
}
