package com.gipl.swayam.ui.addleave;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class AddModifyLeaveFragmentProvider {
    @ContributesAndroidInjector(modules = AddModifyLeaveModule.class)
    abstract AddModifyLeaveFragment addModifyLeaveFragment();
}
