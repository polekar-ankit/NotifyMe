package com.gipl.notifyme.ui.shiftchange;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class ShiftChangeModule {
    @Provides
    ShiftChangeViewModel provideShiftChangeViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        return new ShiftChangeViewModel(dataManager, schedulerProvider);
    }
}
