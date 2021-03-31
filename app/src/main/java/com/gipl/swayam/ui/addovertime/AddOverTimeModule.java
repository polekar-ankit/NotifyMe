package com.gipl.swayam.ui.addovertime;

import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class AddOverTimeModule {
    @Provides
    AddOverTimeViewModel provideAddOverTimeViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        return new AddOverTimeViewModel(dataManager, schedulerProvider);
    }
}
