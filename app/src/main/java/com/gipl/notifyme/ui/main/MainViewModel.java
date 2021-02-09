package com.gipl.notifyme.ui.main;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.domain.SlipDomain;
import com.gipl.notifyme.ui.base.BaseViewModel;
import com.gipl.notifyme.ui.model.Reason;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

public class MainViewModel extends BaseViewModel {
    public MainViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        SlipDomain slipDomain = new SlipDomain(dataManager);
        slipDomain.checkAndRefreshReason(Reason.Type.MISS_PUNCH_REASON);
        slipDomain.checkAndRefreshReason(Reason.Type.SHIFT_CHANGE_REASON);
        slipDomain.checkAndRefreshReason(Reason.Type.OT_REASON);
        slipDomain.checkAndRefreshReason(Reason.Type.CO_REASON);
        slipDomain.checkAndRefreshReason(Reason.Type.LEAVE_REASON);
    }
}
