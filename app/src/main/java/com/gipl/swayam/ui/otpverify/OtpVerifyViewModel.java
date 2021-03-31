package com.gipl.swayam.ui.otpverify;

import android.content.Intent;

import androidx.databinding.ObservableField;

import com.gipl.swayam.R;
import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.data.model.api.ApiError;
import com.gipl.swayam.data.model.api.sendotp.User;
import com.gipl.swayam.data.model.api.verifyotp.VerifyOtpReq;
import com.gipl.swayam.domain.UserUseCase;
import com.gipl.swayam.exceptions.CustomException;
import com.gipl.swayam.ui.base.BaseViewModel;
import com.gipl.swayam.ui.model.Response;
import com.gipl.swayam.uility.AppUtility;
import com.gipl.swayam.uility.rx.SchedulerProvider;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Calendar;

public class OtpVerifyViewModel extends BaseViewModel {
    private final UserUseCase userUseCase;
    private User user;
    private final ObservableField<String> otp = new ObservableField<>("");
    private final ObservableField<String> mobileNumber = new ObservableField<>();

    public OtpVerifyViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        userUseCase = new UserUseCase(dataManager);
    }

    public ObservableField<String> getMobileNumber() {
        return mobileNumber;
    }

    public ObservableField<String> getOtp() {
        return otp;
    }

    public void setData(Intent data) {
        user = data.getParcelableExtra(AppUtility.INTENT_EXTRA.KEY_USER);
        mobileNumber.set(user.getMobileNumber().replaceAll("\\d(?=(?:\\D*\\d){4})", "*"));
    }

    /**
     * generating firebase token
     */
    public void generateFBTokenAndAuth() {
        if (otp.get().isEmpty()) {
            getResponseMutableLiveData().postValue(Response.error(new Exception(new CustomException(getDataManager().getContext().getString(R.string.msg_otp_error)))));
            return;
        }
        getResponseMutableLiveData().setValue(Response.loading());
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                getResponseMutableLiveData().setValue(Response.error(new CustomException(getDataManager().getContext().getString(R.string.exception_not_found))));
                return;
            }
            // Get new Instance ID token
            String token = task.getResult();
            verifyOtp(token);
        });
    }

    private void verifyOtp(String fcmToken) {
        VerifyOtpReq verifyOtpReq = new VerifyOtpReq(user.getEmpId(), otp.get(), fcmToken, "" + Calendar.getInstance().getTime().getTime());
        getCompositeDisposable().add(userUseCase.verifyOtp(verifyOtpReq)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(verifyOtpRsp -> {
                    if (verifyOtpRsp.getApiError().getErrorVal() == ApiError.ERROR_CODE.OK) {
//                        getResponseMutableLiveData().postValue(Response.success(verifyOtpRsp.getUser()));
                        getLib();
//                        getDataManager().setIsLogin();
                    } else {
                        getResponseMutableLiveData().postValue(Response.error(new Exception(new CustomException(verifyOtpRsp.getApiError().getErrorMessage()))));
                    }
                }, throwable -> getResponseMutableLiveData().postValue(Response.error(throwable))));
    }


    public void reSendOtp() {
        getResponseMutableLiveData().postValue(Response.loading());
        getCompositeDisposable().add(userUseCase.sendOtp(user.getEmpId())
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(sendOtpRes -> {
                    if (sendOtpRes.getApiError().getErrorVal() == ApiError.ERROR_CODE.OK) {
                        getResponseMutableLiveData().postValue(Response.success("OTPResend"));
                    } else {
                        getResponseMutableLiveData().postValue(Response.error(new Exception(new CustomException(sendOtpRes.getApiError().getErrorMessage()))));
                    }
                }, throwable -> getResponseMutableLiveData().postValue(Response.error(throwable))));
    }


    private void getLib() {
        getCompositeDisposable().add(userUseCase.getLib().subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(getLibRes -> {
                    if (getLibRes.getApiError().getErrorVal() == ApiError.ERROR_CODE.OK) {
                        getUserShift();
                    } else {
                        getResponseMutableLiveData().postValue(Response.error(new Exception(new CustomException(getLibRes.getApiError().getErrorMessage()))));
                    }
                }, throwable -> getResponseMutableLiveData().postValue(Response.error(throwable))));


    }

    private void getUserShift() {
        getCompositeDisposable().add(userUseCase.getUserShift().subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(getLibRes -> {
                    if (getLibRes.getApiError().getErrorVal() == ApiError.ERROR_CODE.OK) {
                        getDataManager().setIsLogin();
                        getResponseMutableLiveData().postValue(Response.success(getDataManager().getSession()));
                    } else {
                        getResponseMutableLiveData().postValue(Response.error(new Exception(new CustomException(getLibRes.getApiError().getErrorMessage()))));
                    }
                }, throwable -> getResponseMutableLiveData().postValue(Response.error(throwable))));
    }
}