package com.gipl.notifyme.ui.videoplayer;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class PlayerModule {
    @Provides
    PlayerViewModel providesPlayerViewModel(DataManager dataManager, SchedulerProvider schedulerProvider){
        return new PlayerViewModel(dataManager,schedulerProvider);
    }
}
