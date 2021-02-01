package com.gipl.notifyme.ui.addovertime;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class AddOverTimeModule {
    @Provides
    AddOverTimeViewModel provideAddOverTimeViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        return new AddOverTimeViewModel(dataManager, schedulerProvider);
    }
}
