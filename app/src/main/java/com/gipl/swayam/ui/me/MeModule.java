package com.gipl.swayam.ui.me;

import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class MeModule {
    @Provides
    MeViewModel provideMeViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        return new MeViewModel(dataManager, schedulerProvider);
    }
}
