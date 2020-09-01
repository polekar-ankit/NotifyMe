package com.gipl.notifyme.ui.me;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class MeModule {
    @Provides
    MeViewModel provideMeViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        return new MeViewModel(dataManager, schedulerProvider);
    }
}
