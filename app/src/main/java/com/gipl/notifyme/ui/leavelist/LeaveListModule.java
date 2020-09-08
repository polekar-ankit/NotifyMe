package com.gipl.notifyme.ui.leavelist;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class LeaveListModule {
    @Provides
    LeaveListViewModel provideLeaveListViewModel(DataManager dataManager, SchedulerProvider schedulerProvider){
        return new LeaveListViewModel(dataManager, schedulerProvider);
    }
}
