package com.gipl.notifyme.ui.changelng;


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
        return getDataManager().getLanguageCode();
    }
}
