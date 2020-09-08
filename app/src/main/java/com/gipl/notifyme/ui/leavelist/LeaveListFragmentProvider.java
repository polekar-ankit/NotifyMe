package com.gipl.notifyme.ui.leavelist;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class LeaveListFragmentProvider {
    @ContributesAndroidInjector(modules = LeaveListModule.class)
    abstract LeaveListFragment provideLeaveListFragment();
}
