package com.gipl.notifyme.ui.checkin;

import androidx.lifecycle.MutableLiveData;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.data.model.api.ApiError;
import com.gipl.notifyme.data.model.api.checkin.CheckInRsp;
import com.gipl.notifyme.data.model.api.lib.Shifts;
import com.gipl.notifyme.domain.UserUseCase;
import com.gipl.notifyme.exceptions.CustomException;
import com.gipl.notifyme.ui.base.BaseViewModel;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;

public class CheckInViewModel extends BaseViewModel {
    private MutableLiveData<ArrayList<Shifts>> shiftLiveData = new MutableLiveData<>();
    private UserUseCase userUseCase;

    public CheckInViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        userUseCase = new UserUseCase(dataManager);
        getShiftData();
    }

    public MutableLiveData<ArrayList<Shifts>> getShiftLiveData() {
        return shiftLiveData;
    }

    private void getShiftData() {
        shiftLiveData.postValue((ArrayList<Shifts>) getDataManager().getShiftList());
    }

    public void checkIn(String suidShift) {
        getCompositeDisposable().add(userUseCase.checkIn(suidShift)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(checkInRsp -> {
                    if (checkInRsp.getApiError().getErrorVal() == ApiError.ERROR_CODE.OK) {
                        getResponseMutableLiveData().postValue(Response.success(true));
                    } else {
                        getResponseMutableLiveData().postValue(Response.error(new Exception(new CustomException(checkInRsp.getApiError().getErrorMessage()))));
                    }
                }, throwable -> getResponseMutableLiveData().postValue(Response.error(throwable))));
    }
}
