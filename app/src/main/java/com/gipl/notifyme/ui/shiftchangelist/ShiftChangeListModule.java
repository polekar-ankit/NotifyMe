package com.gipl.notifyme.ui.shiftchangelist;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class ShiftChangeListModule {
    @Provides
    ShiftChangeListViewModel provideShiftChangeListViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        return new ShiftChangeListViewModel(dataManager, schedulerProvider);
    }
}
