package com.gipl.notifyme.ui.shiftchangelist;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.data.model.api.ApiError;
import com.gipl.notifyme.data.model.api.shiftchangelist.ShiftChangeListReq;
import com.gipl.notifyme.data.model.api.shiftchangelist.ShiftChangeListRsp;
import com.gipl.notifyme.domain.SlipDomain;
import com.gipl.notifyme.exceptions.CustomException;
import com.gipl.notifyme.ui.base.BaseViewModel;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.uility.TimeUtility;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

import org.json.JSONException;

import io.reactivex.functions.Consumer;

public class ShiftChangeListViewModel extends BaseViewModel {
    private final SlipDomain slipDomain;

    public ShiftChangeListViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        slipDomain = new SlipDomain(dataManager);
    }

    public void getShiftChangeList() {
        try {
            ShiftChangeListReq req = new ShiftChangeListReq();
            req.setSuidSession(getDataManager().getSession());
            req.setPagination(false);
            req.setFilter(getDataManager().getEmpCode());
            req.setjCount(10);
            req.setjStart(0);
            req.setTag(TimeUtility.getCurrentUtcDateTimeForApi());
            getResponseMutableLiveData().postValue(Response.loading());

            getCompositeDisposable().add(slipDomain.getShiftChangeList(req)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .subscribe(rsp -> {
                        if (rsp.getApiError().getErrorVal() == ApiError.ERROR_CODE.OK) {
                            getResponseMutableLiveData().postValue(Response.success(rsp.getScr()));
                        } else {
                            getResponseMutableLiveData().postValue(Response.error(new Exception(new CustomException(rsp.getApiError().getErrorMessage()))));
                        }
                    }, throwable -> getResponseMutableLiveData().postValue(Response.error(throwable)))
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
