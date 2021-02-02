package com.gipl.notifyme.ui.shiftchange;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ShiftChangeFragmentProvider {
    @ContributesAndroidInjector(modules = ShiftChangeModule.class)
    abstract ShiftChangeFragment provideShiftChangeFragment();
}
