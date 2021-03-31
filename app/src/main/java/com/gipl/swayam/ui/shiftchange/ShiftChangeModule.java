package com.gipl.swayam.ui.shiftchange;

import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class ShiftChangeModule {
    @Provides
    ShiftChangeViewModel provideShiftChangeViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        return new ShiftChangeViewModel(dataManager, schedulerProvider);
    }
}
