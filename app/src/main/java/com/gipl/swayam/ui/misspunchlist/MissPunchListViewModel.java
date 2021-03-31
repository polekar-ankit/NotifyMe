package com.gipl.swayam.ui.misspunchlist;

import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.data.model.api.ApiError;
import com.gipl.swayam.data.model.api.mispunchlist.MissPunchListReq;
import com.gipl.swayam.domain.SlipDomain;
import com.gipl.swayam.exceptions.CustomException;
import com.gipl.swayam.ui.base.BaseViewModel;
import com.gipl.swayam.ui.model.Response;
import com.gipl.swayam.uility.TimeUtility;
import com.gipl.swayam.uility.rx.SchedulerProvider;

import org.json.JSONException;

public class MissPunchListViewModel extends BaseViewModel {
    private final SlipDomain slipDomain;

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
        try {
            missPunchListReq.setFilter(getDataManager().getEmpCode());


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

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
