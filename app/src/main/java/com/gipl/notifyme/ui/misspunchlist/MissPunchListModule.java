package com.gipl.notifyme.ui.misspunchlist;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class MissPunchListModule {
    @Provides
    MissPunchListViewModel provideMissPunchListViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        return new MissPunchListViewModel(dataManager, schedulerProvider);
    }
}
