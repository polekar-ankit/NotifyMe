package com.gipl.notifyme.ui.me;

import android.os.CountDownTimer;

import androidx.databinding.ObservableField;

import com.gipl.notifyme.R;
import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.data.model.api.ApiError;
import com.gipl.notifyme.data.model.api.lib.utility.CheckOutType;
import com.gipl.notifyme.data.model.api.sendotp.User;
import com.gipl.notifyme.domain.UserUseCase;
import com.gipl.notifyme.exceptions.CustomException;
import com.gipl.notifyme.ui.base.BaseViewModel;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.uility.TimeUtility;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

public class MeViewModel extends BaseViewModel {
    private ObservableField<String> empName = new ObservableField<>("");
    private ObservableField<String> empCode = new ObservableField<>("");
    private ObservableField<String> empPlant = new ObservableField<>("");
    private ObservableField<String> empMoNumber = new ObservableField<>("");
    private ObservableField<String> empImage = new ObservableField<>("https://preview.keenthemes.com/conquer/assets/img/profile/profile-img.png");
    private UserUseCase userUseCase;
    private CountDownTimer countDownTimer;
    private ObservableField<String> checkInDateTime = new ObservableField<>("");
    private ObservableField<String> checkInTime = new ObservableField<>("");

    public MeViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        userUseCase = new UserUseCase(dataManager);
        User user = dataManager.getUserObj();
        empName.set(user.getName());
        empCode.set(user.getEmpId());
        empMoNumber.set(user.getMobileNumber());
        empPlant.set("Gadre");
    }

    public ObservableField<String> getCheckInDateTime() {
        return checkInDateTime;
    }

    public ObservableField<String> getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTimer() {
        if (!getDataManager().getActiveShift().isEmpty()) {
            long checkInMili = getDataManager().getCheckInTime();
            checkInDateTime.set(TimeUtility.convertUtcMilisecondToDisplay(checkInMili));
            if (countDownTimer != null)
                countDownTimer.cancel();
            countDownTimer = new CountDownTimer(TimeUtility.getDiff(checkInMili), 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    checkInTime.set(TimeUtility.getCountDownTimer(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                }
            };
            countDownTimer.start();
        } else checkInDateTime.set("");
    }

    public void endTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public void checkOut(int type) {
        getResponseMutableLiveData().postValue(Response.loading());
        getCompositeDisposable().add(userUseCase.checkOut(type)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(checkOutRsp -> {
                    if (checkOutRsp.getApiError().getErrorVal() == ApiError.ERROR_CODE.OK) {
                        getDataManager().setCheckInTime(0);
                        getDataManager().setActiveShift("");
                        endTimer();
                        checkInDateTime.set("");
                        checkInTime.set("");
                        CheckOutType checkOutType = getDataManager().getUtility().getCheckOutType();
                        if (type == checkOutType.getBitDayEnd())
                            getResponseMutableLiveData().postValue(Response.success(R.string.day_end));
                        else if (type == checkOutType.getBitLunch())
                            getResponseMutableLiveData().postValue(Response.success(R.string.lunch_out));
                        else if (type == checkOutType.getBitOfficialOut())
                            getResponseMutableLiveData().postValue(Response.success(R.string.official_out));
                    } else {
                        getResponseMutableLiveData().postValue(Response.error(new Exception(new CustomException(checkOutRsp.getApiError().getErrorMessage()))));
                    }
                }, throwable -> getResponseMutableLiveData().postValue(Response.error(throwable))));
    }


    public ObservableField<String> getEmpImage() {
        return empImage;
    }

    public ObservableField<String> getEmpName() {
        return empName;
    }

    public ObservableField<String> getEmpCode() {
        return empCode;
    }

    public ObservableField<String> getEmpPlant() {
        return empPlant;
    }

    public ObservableField<String> getEmpMoNumber() {
        return empMoNumber;
    }
}
