package com.gipl.notifyme.ui.addovertime;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class AddOverTimeFragmentProvider {
    @ContributesAndroidInjector(modules = AddOverTimeModule.class)
    abstract AddOverTimeFragment provideAddOverTimeFragment();
}
