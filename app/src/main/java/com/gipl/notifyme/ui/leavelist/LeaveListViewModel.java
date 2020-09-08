package com.gipl.notifyme.ui.leavelist;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.data.model.api.ApiError;
import com.gipl.notifyme.data.model.api.leaves.GetLeaveRsp;
import com.gipl.notifyme.data.model.api.leaves.GetLeavesReq;
import com.gipl.notifyme.domain.LeaveDomain;
import com.gipl.notifyme.exceptions.CustomException;
import com.gipl.notifyme.ui.base.BaseViewModel;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.uility.TimeUtility;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

import io.reactivex.functions.Consumer;

public class LeaveListViewModel extends BaseViewModel {
    public LeaveListViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        leaveDomain = new LeaveDomain(dataManager);
    }

    private LeaveDomain leaveDomain;

    public void getLeaveList() {
        GetLeavesReq getLeavesReq = new GetLeavesReq();
        getLeavesReq.setPagination(false);
        getLeavesReq.setSuidSession(getDataManager().getSession());
        getLeavesReq.setStart(0);
        getLeavesReq.setTag(TimeUtility.getCurrentUtcDateTimeForApi());
        getResponseMutableLiveData().postValue(Response.loading());
        getCompositeDisposable().add(leaveDomain.getLeaveList(getLeavesReq)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(getLeaveRsp -> {
                    if (getLeaveRsp.getApiError().getErrorVal() == ApiError.ERROR_CODE.OK) {
                        getResponseMutableLiveData().postValue(Response.success(getLeaveRsp.getLeaveRequestArrayList()));
                    } else
                        getResponseMutableLiveData().postValue(Response.error(new Exception(new CustomException(getLeaveRsp.getApiError().getErrorMessage()))));
                }, throwable -> getResponseMutableLiveData().postValue(Response.error(throwable))));
    }
}
