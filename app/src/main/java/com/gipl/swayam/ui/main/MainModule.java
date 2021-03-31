package com.gipl.swayam.ui.main;

import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {
    @Provides
    MainViewModel mainViewModel(DataManager dataManager, SchedulerProvider schedulerProvider){
        return new MainViewModel(dataManager, schedulerProvider);
    }
}
