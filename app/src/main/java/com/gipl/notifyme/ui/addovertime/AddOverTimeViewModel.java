package com.gipl.notifyme.ui.addovertime;

import androidx.databinding.ObservableField;

import com.gipl.notifyme.R;
import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.data.model.api.ApiError;
import com.gipl.notifyme.data.model.api.addovertime.AddOverTimeReq;
import com.gipl.notifyme.data.model.api.addovertime.AddOverTimeRsp;
import com.gipl.notifyme.data.model.api.sendotp.User;
import com.gipl.notifyme.domain.SlipDomain;
import com.gipl.notifyme.exceptions.CustomException;
import com.gipl.notifyme.ui.base.BaseViewModel;
import com.gipl.notifyme.ui.model.Reason;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.uility.TimeUtility;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

import java.text.ParseException;

import io.reactivex.functions.Consumer;

public class AddOverTimeViewModel extends BaseViewModel {
    private SlipDomain slipDomain;
    private ObservableField<String> otHours = new ObservableField<>("");
    private ObservableField<String> otHoursError = new ObservableField<>("");
    private ObservableField<String> otReason = new ObservableField<>("");
    private ObservableField<String> otReasonError = new ObservableField<>("");

    public AddOverTimeViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        slipDomain = new SlipDomain(dataManager);
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
                if (overTime > 16 || overTime < 2) {
                    otHoursError.set("Minimum over time is 2Hr and Maximum overtime is 16Hr.");
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
            addOverTimeReq.setSuidEmployee(user.getSuidEmployee());
            addOverTimeReq.setSuidUserAplicant(user.getSuidEmployee());
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
