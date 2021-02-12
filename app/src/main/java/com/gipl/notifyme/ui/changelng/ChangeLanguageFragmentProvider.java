package com.gipl.notifyme.ui.changelng;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ChangeLanguageFragmentProvider {
    @ContributesAndroidInjector(modules = ChangeLanguageModule.class)
    abstract ChangeLanguageFragment provideChangeLanguageFragment();
}
