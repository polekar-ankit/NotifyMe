package com.gipl.notifyme.ui.colist;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class CoListModule {
    @Provides
    CoListViewModel provideCoListViewModel(DataManager dataManager, SchedulerProvider schedulerProvider){
        return new CoListViewModel(dataManager, schedulerProvider);
    }
}
