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

package com.gipl.notifyme.di.builder;

import com.gipl.notifyme.ui.image.ImagePreviewActivity;
import com.gipl.notifyme.ui.image.ImagePreviewModule;
import com.gipl.notifyme.ui.login.LoginActivity;
import com.gipl.notifyme.ui.login.LoginModule;
import com.gipl.notifyme.ui.notification.NotificationListActivity;
import com.gipl.notifyme.ui.notification.NotificationListModule;
import com.gipl.notifyme.ui.otpverify.OtpVerifyActivity;
import com.gipl.notifyme.ui.otpverify.OtpVerifyModule;
import com.gipl.notifyme.ui.videoplayer.PlayerActivity;
import com.gipl.notifyme.ui.videoplayer.PlayerModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by amitshekhar on 14/09/17.
 */
@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = LoginModule.class)
    abstract LoginActivity provideLoginActivity();

    @ContributesAndroidInjector(modules = OtpVerifyModule.class)
    abstract OtpVerifyActivity provideOtpVerifyActivity();

    @ContributesAndroidInjector(modules = NotificationListModule.class)
    abstract NotificationListActivity provideNotificationListActivity();

    @ContributesAndroidInjector(modules = ImagePreviewModule.class)
    abstract ImagePreviewActivity provideImagePreviewActivity();

    @ContributesAndroidInjector(modules = PlayerModule.class)
    abstract PlayerActivity providePlayerActivity();
}
