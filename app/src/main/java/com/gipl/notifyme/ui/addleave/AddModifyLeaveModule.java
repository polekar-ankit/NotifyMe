package com.gipl.notifyme.ui.addleave;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class AddModifyLeaveModule {
    @Provides
    AddModifyLeaveViewModel provideAddModifyLeaveViewModel(DataManager dataManager, SchedulerProvider schedulerProvider){
        return new AddModifyLeaveViewModel(dataManager, schedulerProvider);
    }
}
