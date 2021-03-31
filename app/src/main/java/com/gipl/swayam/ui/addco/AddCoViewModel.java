package com.gipl.swayam.ui.addco;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;

import com.gipl.swayam.R;
import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.data.model.api.ApiError;
import com.gipl.swayam.data.model.api.addco.AddCoReq;
import com.gipl.swayam.data.model.api.sendotp.User;
import com.gipl.swayam.domain.SlipDomain;
import com.gipl.swayam.exceptions.CustomException;
import com.gipl.swayam.ui.base.BaseViewModel;
import com.gipl.swayam.ui.model.LeaveFor;
import com.gipl.swayam.ui.model.Reason;
import com.gipl.swayam.ui.model.Response;
import com.gipl.swayam.uility.TimeUtility;
import com.gipl.swayam.uility.rx.SchedulerProvider;

import java.util.List;

public class AddCoViewModel extends BaseViewModel {
    private final ObservableField<String> reason = new ObservableField<>("");
    private final ObservableField<String> reasonError = new ObservableField<>("");
    private final SlipDomain slipDomain;

    public AddCoViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        slipDomain = new SlipDomain(dataManager);
        slipDomain.checkAndRefreshReason(Reason.Type.CO_REASON);
    }


    public LiveData<List<Reason>> getPreDefineReasonList() {
        return slipDomain.getReasonLocal(Reason.Type.CO_REASON);
    }

    public ObservableField<String> getReason() {
        return reason;
    }

    public ObservableField<String> getReasonError() {
        return reasonError;
    }

    public void addCo(String coDate, LeaveFor coFor, Reason selectedReason) {
        reasonError.set("");

//        if (selectedReason.getSuid() == -1) {
//            reasonError.set(getDataManager().getContext().getString(R.string.error_co_reason_not_selected));
//        }

        if (/*selectedReason.getSuid() == 32 &&*/ reason.get().isEmpty()) {
            reasonError.set(getDataManager().getContext().getString(R.string.error_co_reason_empty));
        }
        if (coFor.getSuid() == -1 || !reasonError.get().isEmpty()) {
            return;
        }

        AddCoReq req = new AddCoReq();
        req.setSuidSession(getDataManager().getSession());
        req.setTag(TimeUtility.getCurrentUtcDateTimeForApi());
        req.setsDtCO(coDate);
        User user = getDataManager().getUserObj();
        req.setSuidUser(user.getSuidUser());
        req.setSuidUserAplicant(user.getSuidUser());
        req.setjCOFor(coFor.getSuid());
        req.setsReason(/*selectedReason.getSuid() == 32 ? */this.reason.get() /*: selectedReason.getReason()*/);
        req.setEmpCode(getDataManager().getEmpCode());
        getResponseMutableLiveData().postValue(Response.loading());
        getCompositeDisposable().add(slipDomain.addCo(req)
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

    }


}
