package com.gipl.notifyme.ui.splashscreen;

import android.content.res.Resources;
import android.os.Build;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.ui.base.BaseViewModel;
import com.gipl.notifyme.ui.changelng.ChangeLanguageFragment;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

public class SplashScreenViewModel extends BaseViewModel {
    public SplashScreenViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }

}
