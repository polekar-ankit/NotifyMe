package com.gipl.swayam.ui.checkin;

import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class CheckInModule {
    @Provides
    CheckInViewModel checkInViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        return new CheckInViewModel(dataManager, schedulerProvider);
    }
}
