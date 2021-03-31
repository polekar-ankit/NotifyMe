package com.gipl.swayam.ui.login;

import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginModule {
    @Provides
    LoginViewModel provideLoginViewModel(DataManager dataManager, SchedulerProvider schedulerProvider){
        return new LoginViewModel(dataManager, schedulerProvider);
    }
}
