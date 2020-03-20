package com.gipl.notifyme.domain;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.data.model.api.sendotp.SendOTPReq;
import com.gipl.notifyme.data.model.api.sendotp.SendOtpRes;

import org.json.JSONException;

import java.util.Calendar;

import io.reactivex.Single;

public class UserUseCase extends UseCase {

    public UserUseCase(DataManager dataManager) {
        super(dataManager);
    }

    public Single<SendOtpRes> sendOtp(String empId) throws JSONException {
        SendOTPReq sendOTPReq = new SendOTPReq(empId, "" + Calendar.getInstance().getTime().getTime());
        return dataManager.sendOtp(sendOTPReq);
    }
}
