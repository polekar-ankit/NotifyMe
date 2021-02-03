package com.gipl.notifyme.ui.addco;

import androidx.databinding.ObservableField;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.data.model.api.ApiError;
import com.gipl.notifyme.data.model.api.addco.AddCoReq;
import com.gipl.notifyme.data.model.api.addco.AddCoRsp;
import com.gipl.notifyme.data.model.api.sendotp.User;
import com.gipl.notifyme.domain.SlipDomain;
import com.gipl.notifyme.exceptions.CustomException;
import com.gipl.notifyme.ui.base.BaseViewModel;
import com.gipl.notifyme.ui.model.LeaveFor;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.uility.TimeUtility;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

import java.sql.Time;
import java.text.ParseException;

import io.reactivex.functions.Consumer;

public class AddCoViewModel extends BaseViewModel {
    private ObservableField<String>reason = new ObservableField<>("");
    private ObservableField<String>reasonError = new ObservableField<>("");

    private SlipDomain slipDomain;

    public AddCoViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        slipDomain = new SlipDomain(dataManager);
    }

    public ObservableField<String> getReason() {
        return reason;
    }

    public ObservableField<String> getReasonError() {
        return reasonError;
    }

    public void addCo(String coDate, LeaveFor coFor){
        if (coFor.getSuid()==-1){
            return;
        }
        AddCoReq req = new AddCoReq();
        req.setSuidSession(getDataManager().getSession());
        req.setTag(TimeUtility.getCurrentUtcDateTimeForApi());
        req.setsDtCO(coDate);
        User user = getDataManager().getUserObj();
        req.setSuidEmployee(user.getSuidEmployee());
        req.setSuidUserAplicant(user.getSuidEmployee());
        req.setjCOFor(coFor.getSuid());
        req.setsReason(reason.get());
        req.setEmpCode(getDataManager().getEmpCode());
        getResponseMutableLiveData().postValue(Response.loading());
        getCompositeDisposable().add(slipDomain.addCo(req)
        .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(rsp -> {
                    if (rsp.getApiError().getErrorVal() == ApiError.ERROR_CODE.OK) {
                        getResponseMutableLiveData().postValue(Response.success(true));
                    } else {
                        getResponseMutableLiveData().postValue(Response.success(new Exception(new CustomException(rsp.getApiError().getErrorMessage()))));
                    }
                }, throwable -> getResponseMutableLiveData().postValue(Response.error(throwable)))
        );

    }


}
