package com.gipl.notifyme.ui.changelng;


import android.content.res.Resources;
import android.os.Build;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.ui.base.BaseViewModel;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

public class ChangeLanguageViewModel extends BaseViewModel {
    public ChangeLanguageViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }

    public void setLanguageCode(String code) {
        getDataManager().setLanguageCode(code);
    }


    public String getLanguageCode() {
//        String sysLanguageCode;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            sysLanguageCode = Resources.getSystem().getConfiguration().getLocales().get(0).getLanguage();
//        } else {
//            sysLanguageCode = Resources.getSystem().getConfiguration().locale.getLanguage();
//        }
//        String appLanguageCode = getDataManager().getLanguageCode();
//        if (sysLanguageCode.equals(appLanguageCode)) {
//            return appLanguageCode;
//        }
//        else {
//            if (sysLanguageCode.equals(ChangeLanguageFragment.englishCode) ||
//                    sysLanguageCode.equals(ChangeLanguageFragment.marathiCode)) {
//                getDataManager().setLanguageCode(sysLanguageCode);
//            }
//            return sysLanguageCode;
//        }
        return getDataManager().getLanguageCode();

    }
}
