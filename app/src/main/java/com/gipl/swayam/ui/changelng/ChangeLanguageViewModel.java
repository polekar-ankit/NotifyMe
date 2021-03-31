package com.gipl.swayam.ui.changelng;


import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.ui.base.BaseViewModel;
import com.gipl.swayam.uility.rx.SchedulerProvider;

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
