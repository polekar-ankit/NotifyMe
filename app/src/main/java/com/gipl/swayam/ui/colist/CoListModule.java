package com.gipl.swayam.ui.colist;

import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class CoListModule {
    @Provides
    CoListViewModel provideCoListViewModel(DataManager dataManager, SchedulerProvider schedulerProvider){
        return new CoListViewModel(dataManager, schedulerProvider);
    }
}
