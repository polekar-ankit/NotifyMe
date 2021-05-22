package com.gipl.swayam.ui.leavelist;

import androidx.lifecycle.MutableLiveData;

import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.data.model.api.ApiError;
import com.gipl.swayam.data.model.api.leavebalance.LeaveBalance;
import com.gipl.swayam.data.model.api.leaves.GetLeavesReq;
import com.gipl.swayam.domain.LeaveDomain;
import com.gipl.swayam.exceptions.CustomException;
import com.gipl.swayam.ui.base.BaseViewModel;
import com.gipl.swayam.ui.model.Response;
import com.gipl.swayam.uility.TimeUtility;
import com.gipl.swayam.uility.rx.SchedulerProvider;

import java.util.ArrayList;

public class LeaveListViewModel extends BaseViewModel {
    private final LeaveDomain leaveDomain;
    private MutableLiveData<ArrayList<LeaveBalance>> leaveBalanceLiveData = new MutableLiveData<>();

    public LeaveListViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        leaveDomain = new LeaveDomain(dataManager);
    }

    public MutableLiveData<ArrayList<LeaveBalance>> getLeaveBalanceLiveData() {
        return leaveBalanceLiveData;
    }

    public void getLeaveBalance() {
        getLeaveBalanceLiveData().postValue(getDataManager().getLeaveBalance());
        getCompositeDisposable().add(new LeaveDomain(getDataManager()).getLeaveBalance()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(leaveBalanceRsp -> {
                    if (leaveBalanceRsp.getApiError().getErrorVal() == ApiError.ERROR_CODE.OK) {
                        leaveBalanceLiveData.postValue(leaveBalanceRsp.getBalanceArrayList());
                        getResponseMutableLiveData().postValue(Response.success(true));
                    } else {
                        leaveBalanceLiveData.postValue(new ArrayList<>());
                        getResponseMutableLiveData().postValue(Response.error(new Exception(new CustomException(leaveBalanceRsp.getApiError().getErrorMessage()))));
                    }
                }, throwable -> getResponseMutableLiveData().postValue(Response.error(throwable))));
    }

    public void getLeaveList() {
        GetLeavesReq getLeavesReq = new GetLeavesReq();
        getLeavesReq.setPagination(false);
        getLeavesReq.setSuidSession(getDataManager().getSession());
        getLeavesReq.setjStart(0);
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
