package com.gipl.notifyme.ui.otlist;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class OvertimeListModule {
    @Provides
    OvertimeListViewModel provideOvertimeListViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        return new OvertimeListViewModel(dataManager, schedulerProvider);
    }
}
