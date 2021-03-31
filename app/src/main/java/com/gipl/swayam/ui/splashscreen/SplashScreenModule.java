package com.gipl.swayam.ui.splashscreen;

import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class SplashScreenModule {
    @Provides
    SplashScreenViewModel splashScreenViewModel(DataManager dataManager, SchedulerProvider schedulerProvider){
        return new SplashScreenViewModel(dataManager, schedulerProvider);
    }
}
