package com.gipl.notifyme.ui.otlist;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class OvertimeListFragmentProvider {
    @ContributesAndroidInjector(modules = OvertimeListModule.class)
    abstract OvertimeListFragment provideOvertimeListFragment();
}
