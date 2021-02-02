package com.gipl.notifyme.ui.addco;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class AddCoModule {
    @Provides
    AddCoViewModel provideAddCoViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        return new AddCoViewModel(dataManager, schedulerProvider);
    }
}
