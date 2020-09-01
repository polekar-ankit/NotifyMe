package com.gipl.notifyme.ui.checkin;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.ui.base.BaseViewModel;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

public class CheckInViewModel extends BaseViewModel {
    public CheckInViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }
}
