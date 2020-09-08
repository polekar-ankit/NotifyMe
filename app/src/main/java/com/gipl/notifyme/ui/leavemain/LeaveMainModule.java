package com.gipl.notifyme.ui.leavemain;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.ui.main.MainViewModel;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class LeaveMainModule {
    @Provides
    LeaveMainViewModel mainViewModel(DataManager dataManager, SchedulerProvider schedulerProvider){
        return new LeaveMainViewModel(dataManager, schedulerProvider);
    }
}
