package com.gipl.notifyme.ui.notification;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class NotificationListModule {
    @Provides
    NotificationListViewModel provideNotificationListViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        return new NotificationListViewModel(dataManager, schedulerProvider);
    }
}
