package com.gipl.notifyme.ui.main;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {
    @Provides
    MainViewModel mainViewModel(DataManager dataManager, SchedulerProvider schedulerProvider){
        return new MainViewModel(dataManager, schedulerProvider);
    }
}
