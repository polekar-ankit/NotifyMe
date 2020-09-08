package com.gipl.notifyme.domain;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.data.model.api.ApiError;
import com.gipl.notifyme.data.model.api.checkin.CheckInReq;
import com.gipl.notifyme.data.model.api.checkin.CheckInRsp;
import com.gipl.notifyme.data.model.api.checkout.CheckOutReq;
import com.gipl.notifyme.data.model.api.checkout.CheckOutRsp;
import com.gipl.notifyme.data.model.api.lib.GetLibReq;
import com.gipl.notifyme.data.model.api.lib.GetLibRes;
import com.gipl.notifyme.data.model.api.lib.SJson;
import com.gipl.notifyme.data.model.api.lib.Utility;
import com.gipl.notifyme.data.model.api.lib.utility.CheckInType;
import com.gipl.notifyme.data.model.api.lib.utility.CheckOutType;
import com.gipl.notifyme.data.model.api.sendotp.SendOTPReq;
import com.gipl.notifyme.data.model.api.sendotp.SendOtpRes;
import com.gipl.notifyme.data.model.api.verifyotp.VerifyOtpReq;
import com.gipl.notifyme.data.model.api.verifyotp.VerifyOtpRsp;
import com.gipl.notifyme.uility.TimeUtility;
import com.google.gson.Gson;

import java.util.Calendar;

import io.reactivex.Single;

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
            if (verifyOtpRsp.getUser() != null) {
                dataManager.setEmpCode(verifyOtpRsp.getUser().getEmpId());
                dataManager.setUserObj(verifyOtpRsp.getUser());
                dataManager.setSession(verifyOtpRsp.getSession());
            }
            return verifyOtpRsp;
        });
    }

    public Single<GetLibRes> getLib() {
        GetLibReq getLibReq = new GetLibReq();
        getLibReq.setSuidSession(dataManager.getSession());
        getLibReq.setTag(TimeUtility.getCurrentUtcDateTimeForApi());
        return dataManager.getLib(getLibReq).map(getLibRes -> {
            if (getLibRes.getShiftsList() != null && getLibRes.getShiftsList().size() > 0) {
                dataManager.setShiftList(getLibRes.getShiftsList());
            }
            if (getLibRes.getsJson() != null) {
                SJson sJson = new Gson().fromJson(getLibRes.getsJson(),SJson.class);
                dataManager.setUtility(sJson.getUtility());
            } else {
                Utility utility = new Utility();
                utility.setCheckInType(new CheckInType());
                utility.setCheckOutType(new CheckOutType());
            }
            return getLibRes;
        });
    }

    //Todo:need to check paramaeter
    public Single<CheckInRsp> checkIn(String suidShift, String barcode) {
        CheckInReq checkInReq = new CheckInReq();
        checkInReq.setSuidSession(dataManager.getSession());
        checkInReq.setTag(TimeUtility.getCurrentUtcDateTimeForApi());
        checkInReq.setUid(barcode);
        checkInReq.setSuidShift(suidShift);
        checkInReq.setCheckBy(dataManager.getUtility().getCheckInType().getBitBySelf());
        checkInReq.setCheckTime(TimeUtility.getCurrentUtcDateTimeForApi());
        return dataManager.checkIn(checkInReq).map(checkInRsp -> {
            if (checkInRsp.getApiError().getErrorVal() == ApiError.ERROR_CODE.OK) {
                dataManager.setActiveShift(suidShift);
                dataManager.setCheckInTime(TimeUtility.convertUtcTimeToLong(checkInReq.getCheckTime()));
            }
            return checkInRsp;
        });
    }

    //Todo:need to check paramaeter
    public Single<CheckOutRsp> checkOut(int checkOutType) {
        CheckOutReq checkOutReq = new CheckOutReq();
        checkOutReq.setSuidSession(dataManager.getSession());
        checkOutReq.setTag(TimeUtility.getCurrentUtcDateTimeForApi());
        checkOutReq.setCheckTime(TimeUtility.getCurrentUtcDateTimeForApi());
        checkOutReq.setCheckBy(dataManager.getUtility().getCheckInType().getBitBySelf());
        checkOutReq.setSuidShift(dataManager.getActiveShift());
        checkOutReq.setCheckOutType(checkOutType);
        return dataManager.checkOut(checkOutReq);
    }

}
