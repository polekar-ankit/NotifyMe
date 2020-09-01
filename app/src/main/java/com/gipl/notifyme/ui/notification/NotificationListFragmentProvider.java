package com.gipl.notifyme.ui.notification;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class NotificationListFragmentProvider {
    @ContributesAndroidInjector(modules = NotificationListModule.class)
    abstract NotificationListFragment provideNotificationListActivity();
}
