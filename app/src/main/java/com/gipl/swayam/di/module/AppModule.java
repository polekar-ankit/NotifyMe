/*
 *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://mindorks.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.gipl.swayam.di.module;


import android.app.Application;
import android.content.Context;

import com.gipl.swayam.BuildConfig;
import com.gipl.swayam.data.AppDataManager;
import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.data.local.prefs.AppPreferencesHelper;
import com.gipl.swayam.data.local.prefs.PreferencesHelper;
import com.gipl.swayam.data.remote.ApiHelper;
import com.gipl.swayam.data.remote.AppApiHelper;
import com.gipl.swayam.di.PreferenceInfo;
import com.gipl.swayam.uility.rx.AppSchedulerProvider;
import com.gipl.swayam.uility.rx.SchedulerProvider;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by amitshekhar on 07/07/17.
 */
@Module
public class AppModule {

    @Provides
    @Singleton
    ApiHelper provideApiHelper(AppApiHelper appApiHelper) {
        return appApiHelper;
    }



    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application;
    }

    @Provides
    @Singleton
    DataManager provideDataManager(AppDataManager appDataManager) {
        return appDataManager;
    }



    @Provides
    @PreferenceInfo
    String providePreferenceName() {
        return BuildConfig.PREF_NAME;
    }



    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(AppPreferencesHelper appPreferencesHelper) {
        return appPreferencesHelper;
    }


    @Provides
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }




}
