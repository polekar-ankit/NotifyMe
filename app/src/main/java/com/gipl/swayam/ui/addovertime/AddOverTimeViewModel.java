package com.gipl.swayam.ui.addovertime;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;

import com.gipl.swayam.R;
import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.data.model.api.ApiError;
import com.gipl.swayam.data.model.api.addovertime.AddOverTimeReq;
import com.gipl.swayam.data.model.api.sendotp.User;
import com.gipl.swayam.domain.SlipDomain;
import com.gipl.swayam.exceptions.CustomException;
import com.gipl.swayam.ui.base.BaseViewModel;
import com.gipl.swayam.ui.model.Reason;
import com.gipl.swayam.ui.model.Response;
import com.gipl.swayam.uility.TimeUtility;
import com.gipl.swayam.uility.rx.SchedulerProvider;

import java.text.ParseException;
import java.util.List;

public class AddOverTimeViewModel extends BaseViewModel {
    private final SlipDomain slipDomain;
    private final ObservableField<String> otHours = new ObservableField<>("");
    private final ObservableField<String> otHoursError = new ObservableField<>("");
    private final ObservableField<String> otReason = new ObservableField<>("");
    private final ObservableField<String> otReasonError = new ObservableField<>("");


    public AddOverTimeViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        slipDomain = new SlipDomain(dataManager);
        slipDomain.checkAndRefreshReason(Reason.Type.OT_REASON);
    }


    public LiveData<List<Reason>> getPreDefineReasonList() {
        return slipDomain.getReasonLocal(Reason.Type.OT_REASON);
    }

    public ObservableField<String> getOtHoursError() {
        return otHoursError;
    }

    public ObservableField<String> getOtReasonError() {
        return otReasonError;
    }

    public ObservableField<String> getOtHours() {
        return otHours;
    }

    public ObservableField<String> getOtReason() {
        return otReason;
    }

    public void addOverTime(String dtOverTime, Reason reason) {
        try {

            otReasonError.set("");
            otHoursError.set("");

            if (reason.getSuid() == -1) {
                otReasonError.set(getDataManager().getContext().getString(R.string.error_ot_reason_not_selected));
            }
            if (reason.getSuid() == 32 && otReason.get().isEmpty()) {
                otReasonError.set(getDataManager().getContext().getString(R.string.error_ot_reason_empty));
            }

            if (otHours.get().isEmpty()) {
                otHoursError.set(getDataManager().getContext().getString(R.string.error_ot_hr_empty));
            }

            double overTime = 0;
            double intesiveHrs = 0;
            try {
                overTime = Double.parseDouble(otHours.get());
                if (overTime > 16 || overTime < 0.30) {
                    otHoursError.set("Minimum over time is 30 Min and Maximum overtime is 16 Hr.");
                    return;
                }
                if (overTime > 2) {
                    intesiveHrs = overTime - 2;
                    overTime = 2;
                }
            } catch (Exception e) {
                otHoursError.set(getDataManager().getContext().getString(R.string.error_ot_invalid_hr));
            }

            if (!otHoursError.get().isEmpty() || !otReasonError.get().isEmpty()) {
                return;
            }
            AddOverTimeReq addOverTimeReq = new AddOverTimeReq();
            addOverTimeReq.setoTHrs(overTime);
            addOverTimeReq.setIntesiveHrs(intesiveHrs);
            addOverTimeReq.setSuidSession(getDataManager().getSession());
            addOverTimeReq.setDtOverTime(TimeUtility.convertDisplayDateToApi(dtOverTime));
            addOverTimeReq.setEmpCode(getDataManager().getEmpCode());
            User user = getDataManager().getUserObj();
            addOverTimeReq.setSuidPlant(user.getSuidPlant());
            addOverTimeReq.setSuidUser(user.getSuidUser());
            addOverTimeReq.setSuidUserAplicant(user.getSuidUser());
            addOverTimeReq.setsReason(reason.getSuid() == 32 ? otReason.get() : reason.getReason());
            addOverTimeReq.setTag(TimeUtility.getCurrentUtcDateTimeForApi());

            getResponseMutableLiveData().postValue(Response.loading());

            getCompositeDisposable().add(slipDomain.addOverTime(addOverTimeReq)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .subscribe(addOverTimeRsp -> {
                        if (addOverTimeRsp.getApiError().getErrorVal() == ApiError.ERROR_CODE.OK) {
                            getResponseMutableLiveData().postValue(Response.success(true));
                        } else {
                            getResponseMutableLiveData().postValue(Response.error(new Exception(new CustomException(addOverTimeRsp.getApiError().getErrorMessage()))));
                        }
                    }, throwable -> getResponseMutableLiveData().postValue(Response.error(throwable))));

        } catch (ParseException e) {
            e.printStackTrace();
            getResponseMutableLiveData().postValue(Response.error(e));
        }
    }

}
