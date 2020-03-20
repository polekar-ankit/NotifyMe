package com.gipl.notifyme.ui.login;

import androidx.databinding.ObservableField;

import com.gipl.notifyme.R;
import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.data.model.api.ApiError;
import com.gipl.notifyme.data.model.api.sendotp.SendOtpRes;
import com.gipl.notifyme.domain.UserUseCase;
import com.gipl.notifyme.exceptions.CustomException;
import com.gipl.notifyme.ui.base.BaseViewModel;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

import io.reactivex.functions.Consumer;

public class LoginViewModel extends BaseViewModel {
    private UserUseCase userUseCase;
    private ObservableField<String> empId = new ObservableField<>("");
    private ObservableField<String> empIdError = new ObservableField<>();

    public LoginViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        userUseCase = new UserUseCase(dataManager);
    }

    public ObservableField<String> getEmpIdError() {
        return empIdError;
    }

    public ObservableField<String> getEmpId() {
        return empId;
    }



    public void sendOtp() {
        empIdError.set(null);

        if (empId.get().isEmpty()) {
            empIdError.set(getDataManager().getContext().getString(R.string.msg_error_emp_id));
            return;
        }
        getResponseMutableLiveData().postValue(Response.loading());
        getCompositeDisposable().add(userUseCase.sendOtp(empId.get())
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(sendOtpRes -> {
                    if (sendOtpRes.getApiError().getErrorVal() == ApiError.ERROR_CODE.OK) {
                        getResponseMutableLiveData().postValue(Response.success(sendOtpRes.getUser()));
                    } else {
                        getResponseMutableLiveData().postValue(Response.error(new Exception(new CustomException(sendOtpRes.getApiError().getErrorMessage()))));
                    }
                }, throwable -> getResponseMutableLiveData().postValue(Response.error(throwable))));
    }
}
