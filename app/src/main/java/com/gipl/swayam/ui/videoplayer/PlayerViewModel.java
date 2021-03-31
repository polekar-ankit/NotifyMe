package com.gipl.swayam.ui.videoplayer;


import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.ui.base.BaseViewModel;
import com.gipl.swayam.uility.rx.SchedulerProvider;

public class PlayerViewModel extends BaseViewModel {

    public PlayerViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }
}
