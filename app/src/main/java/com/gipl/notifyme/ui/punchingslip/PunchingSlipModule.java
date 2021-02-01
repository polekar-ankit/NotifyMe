package com.gipl.notifyme.ui.punchingslip;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class PunchingSlipModule {

    @Provides
    PunchingSlipViewModel providePunchingSlipViewModel(DataManager dataManager, SchedulerProvider schedulerProvider){
        return new PunchingSlipViewModel(dataManager, schedulerProvider);
    }
}
