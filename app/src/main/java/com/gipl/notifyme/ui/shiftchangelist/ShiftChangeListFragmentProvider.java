package com.gipl.notifyme.ui.shiftchangelist;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ShiftChangeListFragmentProvider {
    @ContributesAndroidInjector(modules = ShiftChangeListModule.class)
    abstract ShiftChangeListFragment provideShiftChangeListFragment();
}
