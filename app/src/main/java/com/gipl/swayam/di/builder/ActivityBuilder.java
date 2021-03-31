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

package com.gipl.swayam.di.builder;

import com.gipl.swayam.ui.addco.AddCoFragmentProvider;
import com.gipl.swayam.ui.addleave.AddModifyLeaveFragmentProvider;
import com.gipl.swayam.ui.addovertime.AddOverTimeFragmentProvider;
import com.gipl.swayam.ui.changelng.ChangeLanguageFragmentProvider;
import com.gipl.swayam.ui.checkin.CheckInActivity;
import com.gipl.swayam.ui.checkin.CheckInModule;
import com.gipl.swayam.ui.colist.CoListFragmentProvider;
import com.gipl.swayam.ui.image.ImagePreviewActivity;
import com.gipl.swayam.ui.image.ImagePreviewModule;
import com.gipl.swayam.ui.leavelist.LeaveListFragmentProvider;
import com.gipl.swayam.ui.login.LoginActivity;
import com.gipl.swayam.ui.login.LoginModule;
import com.gipl.swayam.ui.main.MainActivity;
import com.gipl.swayam.ui.main.MainModule;
import com.gipl.swayam.ui.me.MeFragmentProvider;
import com.gipl.swayam.ui.misspunchlist.MissPunchListFragmentProvider;
import com.gipl.swayam.ui.notification.NotificationListFragmentProvider;
import com.gipl.swayam.ui.otlist.OvertimeListFragmentProvider;
import com.gipl.swayam.ui.otpverify.OtpVerifyActivity;
import com.gipl.swayam.ui.otpverify.OtpVerifyModule;
import com.gipl.swayam.ui.punchingslip.PunchingSlipFragmentProvider;
import com.gipl.swayam.ui.shiftchange.ShiftChangeFragmentProvider;
import com.gipl.swayam.ui.shiftchangelist.ShiftChangeListFragmentProvider;
import com.gipl.swayam.ui.splashscreen.SplashScreenActivity;
import com.gipl.swayam.ui.splashscreen.SplashScreenModule;
import com.gipl.swayam.ui.videoplayer.PlayerActivity;
import com.gipl.swayam.ui.videoplayer.PlayerModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by amitshekhar on 14/09/17.
 */
@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = LoginModule.class)
    abstract LoginActivity provideLoginActivity();

    @ContributesAndroidInjector(modules = SplashScreenModule.class)
    abstract SplashScreenActivity provideSplashScreenActivity();


    @ContributesAndroidInjector(modules = CheckInModule.class)
    abstract CheckInActivity provideCheckInActivity();

    @ContributesAndroidInjector(modules = OtpVerifyModule.class)
    abstract OtpVerifyActivity provideOtpVerifyActivity();

    @ContributesAndroidInjector(modules = {MainModule.class,
            NotificationListFragmentProvider.class,
            MeFragmentProvider.class,
            PunchingSlipFragmentProvider.class,
            MissPunchListFragmentProvider.class,
            LeaveListFragmentProvider.class,
            AddModifyLeaveFragmentProvider.class,
            OvertimeListFragmentProvider.class,
            AddOverTimeFragmentProvider.class,
            ShiftChangeListFragmentProvider.class,
            ShiftChangeFragmentProvider.class,
            AddCoFragmentProvider.class,
            CoListFragmentProvider.class,
            ChangeLanguageFragmentProvider.class
    })
    abstract MainActivity provideMainActivity();

    @ContributesAndroidInjector(modules = ImagePreviewModule.class)
    abstract ImagePreviewActivity provideImagePreviewActivity();

    @ContributesAndroidInjector(modules = PlayerModule.class)
    abstract PlayerActivity providePlayerActivity();
}
