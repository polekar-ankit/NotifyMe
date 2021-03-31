package com.gipl.swayam.ui.addco;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class AddCoFragmentProvider {
    @ContributesAndroidInjector(modules = AddCoModule.class)
    abstract AddCoFragment provideAddCoFragment();
}
