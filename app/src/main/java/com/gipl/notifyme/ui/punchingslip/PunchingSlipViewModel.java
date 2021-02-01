package com.gipl.notifyme.ui.punchingslip;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.data.model.api.ApiError;
import com.gipl.notifyme.data.model.api.lib.Shifts;
import com.gipl.notifyme.data.model.api.punchingslip.AddPunchingSlipReq;
import com.gipl.notifyme.data.model.api.punchingslip.AddPunchingSlipRsp;
import com.gipl.notifyme.domain.SlipDomain;
import com.gipl.notifyme.exceptions.CustomException;
import com.gipl.notifyme.ui.base.BaseViewModel;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.uility.TimeUtility;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

import java.text.ParseException;
import java.util.ArrayList;

import io.reactivex.functions.Consumer;

public class PunchingSlipViewModel extends BaseViewModel {
    private MutableLiveData<ArrayList<Shifts>> shiftLiveData = new MutableLiveData<>();
    private ObservableField<String> shiftError = new ObservableField<>("");
    private SlipDomain slipDomain;

    public PunchingSlipViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        slipDomain = new SlipDomain(dataManager);
        getShiftData();
    }

    public MutableLiveData<ArrayList<Shifts>> getShiftLiveData() {
        return shiftLiveData;
    }

    public ObservableField<String> getShiftError() {
        return shiftError;
    }

    public void setShiftError(String error) {
        shiftError.set(error);
    }

    private void getShiftData() {
        shiftLiveData.postValue((ArrayList<Shifts>) getDataManager().getShiftList());
    }

    public void addPunchingSlip(String inTime, String outTime, String slipDate) {
        try {
            AddPunchingSlipReq req = new AddPunchingSlipReq();
            req.setDtMissPunch(TimeUtility.convertDisplayDateToApi(slipDate));
            req.setInTime(inTime);
            req.setOutTime(outTime);
            getResponseMutableLiveData().postValue(Response.loading());
            getCompositeDisposable().add(slipDomain.addPunchingSlip(req)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .subscribe(rsp -> {
                        if (rsp.getApiError().getErrorVal() == ApiError.ERROR_CODE.OK) {
                            getResponseMutableLiveData().postValue(Response.success(true));
                        } else {
                            getResponseMutableLiveData().postValue(Response.error(new Exception(new CustomException(rsp.getApiError().getErrorMessage()))));
                        }
                    }, throwable -> getResponseMutableLiveData().postValue(Response.error(throwable)))
            );
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
