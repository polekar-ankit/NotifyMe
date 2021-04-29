package com.gipl.swayam.domain;

import android.graphics.Bitmap;

import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.data.model.api.ApiError;
import com.gipl.swayam.data.model.api.checkin.CheckInReq;
import com.gipl.swayam.data.model.api.checkin.CheckInRsp;
import com.gipl.swayam.data.model.api.checkout.CheckOutReq;
import com.gipl.swayam.data.model.api.checkout.CheckOutRsp;
import com.gipl.swayam.data.model.api.dashbordcount.DashboardCountReq;
import com.gipl.swayam.data.model.api.dashbordcount.DashboardCountRsp;
import com.gipl.swayam.data.model.api.lib.GetLibReq;
import com.gipl.swayam.data.model.api.lib.GetLibRes;
import com.gipl.swayam.data.model.api.lib.SJson;
import com.gipl.swayam.data.model.api.lib.Utility;
import com.gipl.swayam.data.model.api.lib.utility.CheckInType;
import com.gipl.swayam.data.model.api.lib.utility.CheckOutType;
import com.gipl.swayam.data.model.api.sendotp.SendOTPReq;
import com.gipl.swayam.data.model.api.sendotp.SendOtpRes;
import com.gipl.swayam.data.model.api.userimg.UserProfileImgReq;
import com.gipl.swayam.data.model.api.userimg.UserProfileImgRsp;
import com.gipl.swayam.data.model.api.usershift.UserShiftReq;
import com.gipl.swayam.data.model.api.usershift.UserShiftRsp;
import com.gipl.swayam.data.model.api.verifyotp.VerifyOtpReq;
import com.gipl.swayam.data.model.api.verifyotp.VerifyOtpRsp;
import com.gipl.swayam.uility.AppUtility;
import com.gipl.swayam.uility.TimeUtility;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.Calendar;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;

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
                SJson sJson = new Gson().fromJson(getLibRes.getsJson(), SJson.class);
                dataManager.setUtility(sJson.getUtility());
            } else {
                Utility utility = new Utility();
                utility.setCheckInType(new CheckInType());
                utility.setCheckOutType(new CheckOutType());
            }
            return getLibRes;
        });
    }

    public Single<UserShiftRsp> getUserShift() {
        UserShiftReq shiftReq = new UserShiftReq();
        shiftReq.setSuidSession(dataManager.getSession());
        shiftReq.setTag(TimeUtility.getCurrentUtcDateTimeForApi());
        return dataManager.getUserShift(shiftReq).map(userShiftRsp -> {
            if (userShiftRsp.getShiftsList() != null && userShiftRsp.getShiftsList().size() > 0) {
                dataManager.setShiftList(userShiftRsp.getShiftsList());
            }
            return userShiftRsp;
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
                dataManager.setCheckType(dataManager.getUtility().getCheckType().getBitCheckIn());
                if (dataManager.getCheckInTime() <= 0)//this means last check out is done for lunch or official out
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

    public Single<Boolean> Logout() {
        return new Single<Boolean>() {
            @Override
            protected void subscribeActual(@NonNull SingleObserver<? super Boolean> observer) {
                dataManager.logout();
                FirebaseMessaging.getInstance().deleteToken();
                dataManager.clearNotificationCache();
//                dataManager.clearAllReason();
                observer.onSuccess(true);
            }
        };
    }

    public Single<DashboardCountRsp> getDashboardCount() throws JSONException {
        DashboardCountReq dashboardCountReq = new DashboardCountReq();
        dashboardCountReq.setSuidSession(dataManager.getSession());
        dashboardCountReq.setTag("");
        return dataManager.getDashboardCount(dashboardCountReq).map(dashboardCountRsp -> {
            if (dashboardCountRsp.getApiError().getErrorVal() == ApiError.ERROR_CODE.OK) {
                dataManager.setDashboardCount(dashboardCountRsp);
            }
            return dashboardCountRsp;
        });
    }

    public Single<UserProfileImgRsp> updateUserProfile(Bitmap img) {
        UserProfileImgReq imgReq = new UserProfileImgReq();
        imgReq.setSuidSession(dataManager.getSession());
        imgReq.setTag(TimeUtility.getCurrentUtcDateTimeForApi());
        imgReq.setsEmpCode(dataManager.getEmpCode());
        imgReq.setsProfileBase64(AppUtility.convertImageToBase64(img));
      return   dataManager.updateUserProfileImg(imgReq);
    }

}
