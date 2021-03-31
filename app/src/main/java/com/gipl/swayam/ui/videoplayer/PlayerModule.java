package com.gipl.swayam.ui.videoplayer;

import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class PlayerModule {
    @Provides
    PlayerViewModel providesPlayerViewModel(DataManager dataManager, SchedulerProvider schedulerProvider){
        return new PlayerViewModel(dataManager,schedulerProvider);
    }
}
