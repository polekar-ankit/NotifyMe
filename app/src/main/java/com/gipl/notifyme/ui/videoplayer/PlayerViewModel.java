package com.gipl.notifyme.ui.videoplayer;


import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.ui.base.BaseViewModel;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

public class PlayerViewModel extends BaseViewModel {

    public PlayerViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }
}
