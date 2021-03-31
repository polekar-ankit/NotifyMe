package com.gipl.swayam.ui.otlist;

import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.data.model.api.ApiError;
import com.gipl.swayam.data.model.api.overtimelist.OverTimeListReq;
import com.gipl.swayam.domain.SlipDomain;
import com.gipl.swayam.exceptions.CustomException;
import com.gipl.swayam.ui.base.BaseViewModel;
import com.gipl.swayam.ui.model.Response;
import com.gipl.swayam.uility.TimeUtility;
import com.gipl.swayam.uility.rx.SchedulerProvider;

import org.json.JSONException;

public class OvertimeListViewModel extends BaseViewModel {
    private final SlipDomain slipDomain;

    public OvertimeListViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        slipDomain = new SlipDomain(dataManager);
    }

    public void getOtList() {
        try {
            OverTimeListReq overTimeListReq = new OverTimeListReq();
            overTimeListReq.setSuidSession(getDataManager().getSession());
            overTimeListReq.setPagination(false);
            overTimeListReq.setjCount(10);
            overTimeListReq.setjStart(0);
            overTimeListReq.setFilter(getDataManager().getEmpCode());
            overTimeListReq.setTag(TimeUtility.getCurrentUtcDateTimeForApi());
            getResponseMutableLiveData().postValue(Response.loading());
            getCompositeDisposable().add(slipDomain.getOverTimeList(overTimeListReq)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .subscribe(rsp -> {
                        if (rsp.getApiError().getErrorVal() == ApiError.ERROR_CODE.OK) {
                            getResponseMutableLiveData().postValue(Response.success(rsp.getOT()));
                        } else {
                            getResponseMutableLiveData().postValue(Response.error(new Exception(new CustomException(rsp.getApiError().getErrorMessage()))));
                        }
                    }, throwable -> getResponseMutableLiveData().postValue(Response.error(throwable))));
        } catch (JSONException e) {
            e.printStackTrace();
            getResponseMutableLiveData().postValue(Response.error(e));
        }
    }
}
