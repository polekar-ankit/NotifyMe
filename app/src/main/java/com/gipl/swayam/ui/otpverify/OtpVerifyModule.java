package com.gipl.swayam.ui.otpverify;

import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class OtpVerifyModule {
    @Provides
    OtpVerifyViewModel provideOtpVerifyViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        return new OtpVerifyViewModel(dataManager, schedulerProvider);
    }
}
