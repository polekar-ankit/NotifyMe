package com.gipl.notifyme.ui.changelng;



import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class ChangeLanguageModule {
    @Provides
    ChangeLanguageViewModel provideChangeLanguageViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        return new ChangeLanguageViewModel(dataManager, schedulerProvider);
    }
}
