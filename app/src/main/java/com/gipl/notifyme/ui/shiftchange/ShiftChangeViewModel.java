package com.gipl.notifyme.ui.shiftchange;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gipl.notifyme.R;
import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.data.FirebaseDb;
import com.gipl.notifyme.data.model.api.ApiError;
import com.gipl.notifyme.data.model.api.lib.Shifts;
import com.gipl.notifyme.data.model.api.sendotp.User;
import com.gipl.notifyme.data.model.api.shiftchange.ShiftChangeReq;
import com.gipl.notifyme.data.model.api.shiftchange.ShiftChangeRsp;
import com.gipl.notifyme.domain.SlipDomain;
import com.gipl.notifyme.exceptions.CustomException;
import com.gipl.notifyme.ui.base.BaseViewModel;
import com.gipl.notifyme.ui.model.Reason;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.uility.TimeUtility;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class ShiftChangeViewModel extends BaseViewModel {
    private final MutableLiveData<ArrayList<Shifts>> shiftLiveData = new MutableLiveData<>();
    private final ObservableField<String> reason = new ObservableField<>("");
    private final ObservableField<String> reasonError = new ObservableField<>("");

    private final SlipDomain slipDomain;
    private final ObservableField<String> shiftFromError = new ObservableField<>();
    private final ObservableField<String> shifToError = new ObservableField<>();

    public ShiftChangeViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        getShiftData();
        slipDomain = new SlipDomain(dataManager);
        slipDomain.checkAndRefreshReason(Reason.Type.SHIFT_CHANGE_REASON);
    }


    public LiveData<List<Reason>> getPreDefineReasonList() {
        return slipDomain.getReasonLocal(Reason.Type.SHIFT_CHANGE_REASON);
    }

    public ObservableField<String> getShiftFromError() {
        return shiftFromError;
    }

    public ObservableField<String> getShifToError() {
        return shifToError;
    }

    public ObservableField<String> getReasonError() {
        return reasonError;
    }

    public ObservableField<String> getReason() {
        return reason;
    }

    private void getShiftData() {
        shiftLiveData.postValue((ArrayList<Shifts>) getDataManager().getShiftList());
    }

    public MutableLiveData<ArrayList<Shifts>> getShiftLiveData() {
        return shiftLiveData;
    }

    public void addShiftChangedRequest(String dtFrom, String dtTo, String suidShiftFrom, String suidShiftTo, Reason selectedReason) {
        User user = getDataManager().getUserObj();

        shifToError.set("");
        shiftFromError.set("");
        reasonError.set("");

        if (suidShiftFrom == null) {
            shiftFromError.set(getDataManager().getContext().getString(R.string.from_shift_not_select_error));
        }

        if (suidShiftTo == null) {
            shifToError.set(getDataManager().getContext().getString(R.string.to_shift_not_select_error));
        }
        if (selectedReason.getSuid() == -1) {
            reasonError.set(getDataManager().getContext().getString(R.string.error_shift_change_reason_not_selected));
        }
        if (selectedReason.getSuid() == 32 && this.reason.get().isEmpty()) {
            reasonError.set(getDataManager().getContext().getString(R.string.error_shift_reason_empty));
        }
        if (!shiftFromError.get().isEmpty() || !shifToError.get().isEmpty() || !reasonError.get().isEmpty()) {
            return;
        }

        ShiftChangeReq changeReq = new ShiftChangeReq();
        changeReq.setSuidSession(getDataManager().getSession());
        changeReq.setTag(TimeUtility.getCurrentUtcDateTimeForApi());
        changeReq.setDtShiftFrom(dtFrom);
        changeReq.setDtShiftTo(dtTo);
        changeReq.setsEmpCode(user.getEmpId());
        changeReq.setSuidShiftFrom(suidShiftFrom);
        changeReq.setSuidShiftTo(suidShiftTo);
        changeReq.setSuidUserAplicant(user.getSuidUser());
        changeReq.setSuidPlant(user.getSuidPlant());
        changeReq.setSuidUser(user.getSuidUser());
        changeReq.setReason(selectedReason.getSuid() == 32 ? this.reason.get() : selectedReason.getReason());
        changeReq.setsExtraInfo("");


        getResponseMutableLiveData().postValue(Response.loading());

        getCompositeDisposable().add(slipDomain.addShiftChange(changeReq)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(rsp -> {
                    if (rsp.getApiError().getErrorVal() == ApiError.ERROR_CODE.OK) {
                        getResponseMutableLiveData().postValue(Response.success(true));
                    } else {
                        getResponseMutableLiveData().postValue(Response.error(new Exception(new CustomException(rsp.getApiError().getErrorMessage()))));
                    }
                }, throwable -> getResponseMutableLiveData().postValue(Response.error(throwable))));
    }
}
