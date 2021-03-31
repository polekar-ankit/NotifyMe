package com.gipl.swayam.ui.login;

import androidx.databinding.ObservableField;

import com.gipl.swayam.BuildConfig;
import com.gipl.swayam.R;
import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.data.model.api.ApiError;
import com.gipl.swayam.domain.UserUseCase;
import com.gipl.swayam.exceptions.CustomException;
import com.gipl.swayam.ui.base.BaseViewModel;
import com.gipl.swayam.ui.model.Response;
import com.gipl.swayam.uility.rx.SchedulerProvider;

public class LoginViewModel extends BaseViewModel {
    private final UserUseCase userUseCase;
    private final ObservableField<String> empId = new ObservableField<>("");
    private final ObservableField<String> empIdError = new ObservableField<>();

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

    public String value = "";

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
                        if (BuildConfig.DEBUG)
                            value = sendOtpRes.getMessage();
                        getResponseMutableLiveData().postValue(Response.success(sendOtpRes.getUser()));
                    } else {
                        getResponseMutableLiveData().postValue(Response.error(new Exception(new CustomException(sendOtpRes.getApiError().getErrorMessage()))));
                    }
                }, throwable -> getResponseMutableLiveData().postValue(Response.error(throwable))));
    }
}