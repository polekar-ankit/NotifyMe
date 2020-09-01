package com.gipl.notifyme.ui.checkin;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class CheckInModule {
    @Provides
    CheckInViewModel checkInViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        return new CheckInViewModel(dataManager, schedulerProvider);
    }
}
