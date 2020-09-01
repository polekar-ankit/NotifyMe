package com.gipl.notifyme.ui.me;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MeFragmentProvider {
    @ContributesAndroidInjector(modules = MeModule.class)
    abstract MeFragment provideMeFragment();
}
