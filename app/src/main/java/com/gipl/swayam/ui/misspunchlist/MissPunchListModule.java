package com.gipl.swayam.ui.misspunchlist;

import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class MissPunchListModule {
    @Provides
    MissPunchListViewModel provideMissPunchListViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        return new MissPunchListViewModel(dataManager, schedulerProvider);
    }
}
