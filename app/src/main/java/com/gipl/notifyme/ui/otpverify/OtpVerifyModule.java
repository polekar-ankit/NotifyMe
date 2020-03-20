package com.gipl.notifyme.ui.otpverify;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class OtpVerifyModule {
    @Provides
    OtpVerifyViewModel provideOtpVerifyViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        return new OtpVerifyViewModel(dataManager, schedulerProvider);
    }
}
