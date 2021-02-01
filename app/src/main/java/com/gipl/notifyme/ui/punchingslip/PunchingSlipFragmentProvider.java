package com.gipl.notifyme.ui.punchingslip;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class PunchingSlipFragmentProvider {
    @ContributesAndroidInjector(modules = PunchingSlipModule.class)
    abstract PunchingSlipFragment providePunchingSlipFragment();
}
