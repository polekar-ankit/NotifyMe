package com.gipl.notifyme.ui.otpverify;

import android.content.Intent;
import android.util.Log;

import androidx.databinding.ObservableField;

import com.gipl.notifyme.R;
import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.data.model.api.ApiError;
import com.gipl.notifyme.data.model.api.sendotp.User;
import com.gipl.notifyme.data.model.api.verifyotp.VerifyOtpReq;
import com.gipl.notifyme.data.model.api.verifyotp.VerifyOtpRsp;
import com.gipl.notifyme.domain.UserUseCase;
import com.gipl.notifyme.exceptions.CustomException;
import com.gipl.notifyme.ui.base.BaseViewModel;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.uility.AppUtility;
import com.gipl.notifyme.uility.rx.SchedulerProvider;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Calendar;

import io.reactivex.functions.Consumer;

public class OtpVerifyViewModel extends BaseViewModel {
    private UserUseCase userUseCase;
    private User user;
    private ObservableField<String> otp = new ObservableField<>("");
    private ObservableField<String> mobileNumber = new ObservableField<>();

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
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        getResponseMutableLiveData().setValue(Response.error(new CustomException(getDataManager().getContext().getString(R.string.exception_not_found))));
                        return;
                    }
                    // Get new Instance ID token
                    String token = task.getResult().getToken();
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
                        getResponseMutableLiveData().postValue(Response.success(verifyOtpRsp.getUser()));
                        getDataManager().setIsLogin();
                    } else {
                        getResponseMutableLiveData().postValue(Response.error(new Exception(new CustomException(verifyOtpRsp.getApiError().getErrorMessage()))));
                    }
                }, throwable -> getResponseMutableLiveData().postValue(Response.error(throwable))));
    }
}
