package com.gipl.swayam.ui.notification;

import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class NotificationListModule {
    @Provides
    NotificationListViewModel provideNotificationListViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        return new NotificationListViewModel(dataManager, schedulerProvider);
    }
}
