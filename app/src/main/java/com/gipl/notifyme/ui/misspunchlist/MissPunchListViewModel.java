package com.gipl.notifyme.ui.misspunchlist;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.data.model.api.ApiError;
import com.gipl.notifyme.data.model.api.mispunchlist.MissPunchListReq;
import com.gipl.notifyme.data.model.api.mispunchlist.MissPunchListRsp;
import com.gipl.notifyme.domain.SlipDomain;
import com.gipl.notifyme.exceptions.CustomException;
import com.gipl.notifyme.ui.base.BaseViewModel;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.uility.TimeUtility;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

import io.reactivex.functions.Consumer;

public class MissPunchListViewModel extends BaseViewModel {
    private SlipDomain slipDomain;

    public MissPunchListViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        slipDomain = new SlipDomain(dataManager);
    }

    public void getMissPunchList() {
        getResponseMutableLiveData().postValue(Response.loading());
        MissPunchListReq missPunchListReq = new MissPunchListReq();
        missPunchListReq.setSuidSession(getDataManager().getSession());
        missPunchListReq.setPagination(false);
        missPunchListReq.setjCount(10);
        missPunchListReq.setjStart(0);
        missPunchListReq.setTag(TimeUtility.getCurrentUtcDateTimeForApi());
        getCompositeDisposable().add(slipDomain.getMissPunchList(missPunchListReq)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(missPunchListRsp -> {
                    if (missPunchListRsp.getApiError().getErrorVal() == ApiError.ERROR_CODE.OK) {
                        getResponseMutableLiveData().postValue(Response.success(missPunchListRsp.getLiMissPunch()));
                    } else {
                        getResponseMutableLiveData().postValue(Response.error(new Exception(new CustomException(missPunchListRsp.getApiError().getErrorMessage()))));
                    }
                }, throwable -> getResponseMutableLiveData().postValue(Response.error(throwable))));
    }
}
