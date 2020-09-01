package com.gipl.notifyme.ui.splashscreen;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class SplashScreenModule {
    @Provides
    SplashScreenViewModel splashScreenViewModel(DataManager dataManager, SchedulerProvider schedulerProvider){
        return new SplashScreenViewModel(dataManager, schedulerProvider);
    }
}
