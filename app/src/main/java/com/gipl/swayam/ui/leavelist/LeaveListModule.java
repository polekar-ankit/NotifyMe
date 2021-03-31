package com.gipl.swayam.ui.leavelist;

import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class LeaveListModule {
    @Provides
    LeaveListViewModel provideLeaveListViewModel(DataManager dataManager, SchedulerProvider schedulerProvider){
        return new LeaveListViewModel(dataManager, schedulerProvider);
    }
}
