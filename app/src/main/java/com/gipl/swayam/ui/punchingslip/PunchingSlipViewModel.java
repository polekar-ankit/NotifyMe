package com.gipl.swayam.ui.punchingslip;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;

import com.gipl.swayam.R;
import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.data.model.api.ApiError;
import com.gipl.swayam.data.model.api.punchingslip.AddPunchingSlipReq;
import com.gipl.swayam.domain.SlipDomain;
import com.gipl.swayam.exceptions.CustomException;
import com.gipl.swayam.ui.base.BaseViewModel;
import com.gipl.swayam.ui.model.Reason;
import com.gipl.swayam.ui.model.Response;
import com.gipl.swayam.uility.TimeUtility;
import com.gipl.swayam.uility.rx.SchedulerProvider;

import java.text.ParseException;
import java.util.List;

public class PunchingSlipViewModel extends BaseViewModel {
    private final ObservableField<String> shiftError = new ObservableField<>("");
    private final ObservableField<String> inTimeError = new ObservableField<>("");
    private final ObservableField<String> outTimeError = new ObservableField<>("");
    private final ObservableField<String> reason = new ObservableField<>("");
    private final ObservableField<String> reasonError = new ObservableField<>();
    private final SlipDomain slipDomain;

    public PunchingSlipViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        slipDomain = new SlipDomain(dataManager);
        slipDomain.checkAndRefreshReason(Reason.Type.MISS_PUNCH_REASON);
    }


    public LiveData<List<Reason>> getPreDefineReasonList() {
        return slipDomain.getReasonLocal(Reason.Type.MISS_PUNCH_REASON);
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

    public ObservableField<String> getShiftError() {
        return shiftError;
    }

    public void setShiftError(String error) {
        shiftError.set(error);
    }


    public void addPunchingSlip(String inTime, String outTime, String slipDate, Reason selectedReason) {
        try {
            shiftError.set("");
            inTimeError.set("");
            outTimeError.set("");
            reasonError.set("");


            if (inTime.isEmpty() && outTime.isEmpty()) {
                inTimeError.set(getDataManager().getContext().getString(R.string.error_in_time));
                outTimeError.set(getDataManager().getContext().getString(R.string.error_out_time));
            }
//            if (selectedReason.getSuid() == -1) {
//                reasonError.set(getDataManager().getContext().getString(R.string.error_mispunch_reason_not_selected));
//            }
            if (/*selectedReason.getSuid() == 32 &&*/ reason.get().isEmpty()) {
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

            req.setsReason(/*selectedReason.getSuid() == 32 ?*/ reason.get() /*: selectedReason.getReason()*/);

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
 