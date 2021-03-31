package com.gipl.swayam.ui.punchingslip;

import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class PunchingSlipModule {

    @Provides
    PunchingSlipViewModel providePunchingSlipViewModel(DataManager dataManager, SchedulerProvider schedulerProvider){
        return new PunchingSlipViewModel(dataManager, schedulerProvider);
    }
}
