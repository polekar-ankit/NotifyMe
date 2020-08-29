package com.gipl.notifyme.domain;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.data.model.api.sendotp.SendOTPReq;
import com.gipl.notifyme.data.model.api.sendotp.SendOtpRes;
import com.gipl.notifyme.data.model.api.verifyotp.VerifyOtpReq;
import com.gipl.notifyme.data.model.api.verifyotp.VerifyOtpRsp;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.Calendar;

import io.reactivex.Single;
import io.reactivex.functions.Function;

public class UserUseCase extends UseCase {

    public UserUseCase(DataManager dataManager) {
        super(dataManager);
    }

    public Single<SendOtpRes> sendOtp(String empId) {
        SendOTPReq sendOTPReq = new SendOTPReq(empId, "" + Calendar.getInstance().getTime().getTime());
        return dataManager.sendOtp(sendOTPReq);
    }

    public Single<VerifyOtpRsp> verifyOtp(VerifyOtpReq verifyOtpReq) {
        return dataManager.verifyOtp(verifyOtpReq).map(verifyOtpRsp -> {
            if (verifyOtpRsp.getUser() != null)
                dataManager.setEmpCode(verifyOtpRsp.getUser().getEmpId());
            return verifyOtpRsp;
        });
    }
}
