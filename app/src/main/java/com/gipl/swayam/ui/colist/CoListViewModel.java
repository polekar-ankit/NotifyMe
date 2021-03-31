package com.gipl.swayam.ui.colist;

import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.data.model.api.ApiError;
import com.gipl.swayam.data.model.api.colist.CoListReq;
import com.gipl.swayam.domain.SlipDomain;
import com.gipl.swayam.exceptions.CustomException;
import com.gipl.swayam.ui.base.BaseViewModel;
import com.gipl.swayam.ui.model.Response;
import com.gipl.swayam.uility.TimeUtility;
import com.gipl.swayam.uility.rx.SchedulerProvider;

import org.json.JSONException;

public class CoListViewModel extends BaseViewModel {
    private final SlipDomain slipDomain;

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
