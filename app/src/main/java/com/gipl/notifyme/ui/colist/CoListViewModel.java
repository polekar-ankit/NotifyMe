package com.gipl.notifyme.ui.colist;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.data.model.api.ApiError;
import com.gipl.notifyme.data.model.api.colist.CoListReq;
import com.gipl.notifyme.data.model.api.colist.CoListRsp;
import com.gipl.notifyme.domain.SlipDomain;
import com.gipl.notifyme.exceptions.CustomException;
import com.gipl.notifyme.ui.base.BaseViewModel;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.uility.TimeUtility;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

import org.json.JSONException;

import io.reactivex.functions.Consumer;

public class CoListViewModel extends BaseViewModel {
    private SlipDomain slipDomain;

    public CoListViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        slipDomain = new SlipDomain(dataManager);
    }

    public void getCOList() {
        try {
            CoListReq req = new CoListReq();
            req.setSuidSession(getDataManager().getSession());
            req.setPagination(false);
            req.setjStart(0);
            req.setjCount(10);
            req.setTag(TimeUtility.getCurrentUtcDateTimeForApi());
            req.setFilter(getDataManager().getEmpCode());
            getResponseMutableLiveData().postValue(Response.loading());
            getCompositeDisposable().add(slipDomain.getCoList(req)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .subscribe(rsp -> {
                        if (rsp.getApiError().getErrorVal() == ApiError.ERROR_CODE.OK) {
                            getResponseMutableLiveData().postValue(Response.success(rsp.getCO()));
                        } else {
                            getResponseMutableLiveData().postValue(Response.error(new Exception(new CustomException(rsp.getApiError().getErrorMessage()))));
                        }
                    }, throwable -> getResponseMutableLiveData().postValue(Response.error(throwable))));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
