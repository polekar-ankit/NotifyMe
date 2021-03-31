package com.gipl.swayam.ui.changelng;



import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class ChangeLanguageModule {
    @Provides
    ChangeLanguageViewModel provideChangeLanguageViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        return new ChangeLanguageViewModel(dataManager, schedulerProvider);
    }
}
