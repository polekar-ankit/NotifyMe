package com.gipl.swayam.ui.addleave;

import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class AddModifyLeaveModule {
    @Provides
    AddModifyLeaveViewModel provideAddModifyLeaveViewModel(DataManager dataManager, SchedulerProvider schedulerProvider){
        return new AddModifyLeaveViewModel(dataManager, schedulerProvider);
    }
}
