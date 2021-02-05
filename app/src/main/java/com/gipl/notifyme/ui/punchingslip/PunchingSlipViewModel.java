package com.gipl.notifyme.ui.punchingslip;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.gipl.notifyme.R;
import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.data.model.api.ApiError;
import com.gipl.notifyme.data.model.api.lib.Shifts;
import com.gipl.notifyme.data.model.api.punchingslip.AddPunchingSlipReq;
import com.gipl.notifyme.data.model.api.punchingslip.AddPunchingSlipRsp;
import com.gipl.notifyme.domain.SlipDomain;
import com.gipl.notifyme.exceptions.CustomException;
import com.gipl.notifyme.ui.base.BaseViewModel;
import com.gipl.notifyme.ui.model.Reason;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.uility.TimeUtility;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

import java.text.ParseException;
import java.util.ArrayList;

import io.reactivex.functions.Consumer;

public class PunchingSlipViewModel extends BaseViewModel {
    private MutableLiveData<ArrayList<Shifts>> shiftLiveData = new MutableLiveData<>();
    private ObservableField<String> shiftError = new ObservableField<>("");
    private ObservableField<String> inTimeError = new ObservableField<>("");
    private ObservableField<String> outTimeError = new ObservableField<>("");
    private ObservableField<String> reason = new ObservableField<>("");
    private ObservableField<String> reasonError = new ObservableField<>();
    private SlipDomain slipDomain;

    public PunchingSlipViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        slipDomain = new SlipDomain(dataManager);
        getShiftData();
    }

    public ObservableField<String> getReason() {
        return reason;
    }

    public ObservableField<String> getReasonError() {
        return reasonError;
    }

    public ObservableField<String> getInTimeError() {
        return inTimeError;
    }

    public ObservableField<String> getOutTimeError() {
        return outTimeError;
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

    public void addPunchingSlip(String inTime, String outTime, String slipDate, String suidShift, Reason selectedReason) {
        try {
            shiftError.set("");
            inTimeError.set("");
            outTimeError.set("");
            reasonError.set("");
            if (suidShift == null) {
                shiftError.set(getDataManager().getContext().getString(R.string.shift_not_select_error));
            }
            if (inTime.isEmpty() && outTime.isEmpty()) {
                inTimeError.set(getDataManager().getContext().getString(R.string.error_in_time));
                outTimeError.set(getDataManager().getContext().getString(R.string.error_out_time));
            }
            if (selectedReason.getSuid() == -1) {
                reasonError.set(getDataManager().getContext().getString(R.string.error_mispunch_reason_not_selected));
            }
            if (selectedReason.getSuid() == 32 && reason.get().isEmpty()) {
                reasonError.set(getDataManager().getContext().getString(R.string.error_mispunch_reason_empty));
            }
            if (!shiftError.get().isEmpty() || !inTimeError.get().isEmpty()
                    || !outTimeError.get().isEmpty() || !reasonError.get().isEmpty()) {
                return;
            }

            AddPunchingSlipReq req = new AddPunchingSlipReq();
            req.setDtMissPunch(TimeUtility.convertDisplayDateToApi(slipDate));
            req.setInTime(inTime);
            req.setOutTime(outTime);
            req.setSuidShift(suidShift);
            req.setsReason(selectedReason.getSuid() == 32 ? reason.get() : selectedReason.getReason());
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
