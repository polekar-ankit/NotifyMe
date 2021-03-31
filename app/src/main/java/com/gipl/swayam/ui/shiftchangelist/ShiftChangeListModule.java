package com.gipl.swayam.ui.shiftchangelist;

import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class ShiftChangeListModule {
    @Provides
    ShiftChangeListViewModel provideShiftChangeListViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        return new ShiftChangeListViewModel(dataManager, schedulerProvider);
    }
}
