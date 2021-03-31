package com.gipl.swayam.ui.misspunchlist;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MissPunchListFragmentProvider {

    @ContributesAndroidInjector(modules = MissPunchListModule.class)
    abstract MissPunchListFragment provideMissPunchListFragment();
}
