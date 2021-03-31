package com.gipl.swayam.ui.otlist;

import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class OvertimeListModule {
    @Provides
    OvertimeListViewModel provideOvertimeListViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        return new OvertimeListViewModel(dataManager, schedulerProvider);
    }
}
