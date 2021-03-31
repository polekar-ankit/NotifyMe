package com.gipl.swayam.ui.addco;

import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class AddCoModule {
    @Provides
    AddCoViewModel provideAddCoViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        return new AddCoViewModel(dataManager, schedulerProvider);
    }
}
