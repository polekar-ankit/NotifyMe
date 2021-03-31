package com.gipl.swayam.ui.colist;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class CoListFragmentProvider {
    @ContributesAndroidInjector(modules = CoListModule.class)
    abstract CoListFragment provideCoListFragment();
}
