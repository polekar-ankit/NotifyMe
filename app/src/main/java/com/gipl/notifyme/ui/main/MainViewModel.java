package com.gipl.notifyme.ui.main;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.ui.base.BaseViewModel;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

public class MainViewModel extends BaseViewModel {
    public MainViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }
}
