/*
 *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://mindorks.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.gipl.notifyme.data.remote;


import com.androidnetworking.BuildConfig;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.gipl.notifyme.data.model.api.addco.AddCoReq;
import com.gipl.notifyme.data.model.api.addco.AddCoRsp;
import com.gipl.notifyme.data.model.api.addovertime.AddOverTimeReq;
import com.gipl.notifyme.data.model.api.addovertime.AddOverTimeRsp;
import com.gipl.notifyme.data.model.api.applyleave.AddModifyLeaveReq;
import com.gipl.notifyme.data.model.api.applyleave.AddModifyLeaveRsp;
import com.gipl.notifyme.data.model.api.checkin.CheckInReq;
import com.gipl.notifyme.data.model.api.checkin.CheckInRsp;
import com.gipl.notifyme.data.model.api.checkout.CheckOutReq;
import com.gipl.notifyme.data.model.api.checkout.CheckOutRsp;
import com.gipl.notifyme.data.model.api.colist.CoListReq;
import com.gipl.notifyme.data.model.api.colist.CoListRsp;
import com.gipl.notifyme.data.model.api.leaves.GetLeaveRsp;
import com.gipl.notifyme.data.model.api.leaves.GetLeavesReq;
import com.gipl.notifyme.data.model.api.leavetype.LeaveTypeReq;
import com.gipl.notifyme.data.model.api.leavetype.LeaveTypeRsp;
import com.gipl.notifyme.data.model.api.lib.GetLibReq;
import com.gipl.notifyme.data.model.api.lib.GetLibRes;
import com.gipl.notifyme.data.model.api.mispunchlist.MissPunchListReq;
import com.gipl.notifyme.data.model.api.mispunchlist.MissPunchListRsp;
import com.gipl.notifyme.data.model.api.notification.GetNotificationRes;
import com.gipl.notifyme.data.model.api.notification.GetNotificationsReq;
import com.gipl.notifyme.data.model.api.overtimelist.OverTimeListReq;
import com.gipl.notifyme.data.model.api.overtimelist.OverTimeListRsp;
import com.gipl.notifyme.data.model.api.punchingslip.AddPunchingSlipReq;
import com.gipl.notifyme.data.model.api.punchingslip.AddPunchingSlipRsp;
import com.gipl.notifyme.data.model.api.sendotp.SendOTPReq;
import com.gipl.notifyme.data.model.api.sendotp.SendOtpRes;
import com.gipl.notifyme.data.model.api.shiftchange.ShiftChangeReq;
import com.gipl.notifyme.data.model.api.shiftchange.ShiftChangeRsp;
import com.gipl.notifyme.data.model.api.shiftchangelist.ShiftChangeListReq;
import com.gipl.notifyme.data.model.api.shiftchangelist.ShiftChangeListRsp;
import com.gipl.notifyme.data.model.api.verifyotp.VerifyOtpReq;
import com.gipl.notifyme.data.model.api.verifyotp.VerifyOtpRsp;
import com.google.gson.Gson;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import okhttp3.OkHttpClient;

@Singleton
public class AppApiHelper implements ApiHelper {


    private OkHttpClient okHttpClient;


    @Inject
    public AppApiHelper() {
//        if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient().newBuilder()
                .addInterceptor(httpLoggingInterceptor)
                .build();
//        } else okHttpClient = new OkHttpClient().newBuilder()
//                .build();
    }


    @Override
    public Single<SendOtpRes> sendOtp(SendOTPReq sendOTPReq) {
        return Rx2AndroidNetworking.post(ApiEndPoint.SEND_OTP)
                .addBodyParameter(sendOTPReq)
                .setOkHttpClient(okHttpClient)
                .build()
                .getObjectSingle(SendOtpRes.class);
    }

    @Override
    public Single<VerifyOtpRsp> verifyOtp(VerifyOtpReq verifyOtpReq) {
        return Rx2AndroidNetworking.post(ApiEndPoint.VERIFY_OTP)
                .addBodyParameter(verifyOtpReq)
                .setOkHttpClient(okHttpClient)
                .build()
                .getObjectSingle(VerifyOtpRsp.class);
    }

    @Override
    public Single<GetLibRes> getLib(GetLibReq getLibReq) {
        return Rx2AndroidNetworking.post(ApiEndPoint.GET_LIB)
                .addBodyParameter(getLibReq)
                .setOkHttpClient(okHttpClient)
                .build()
                .getObjectSingle(GetLibRes.class);
    }

    @Override
    public Single<GetNotificationRes> getNotifications(GetNotificationsReq req) {
        return Rx2AndroidNetworking.post(ApiEndPoint.GET_NOTIFICATION)
                .addBodyParameter(req)
                .setOkHttpClient(okHttpClient)
                .build()
                .getObjectSingle(GetNotificationRes.class);
    }

    @Override
    public Single<CheckInRsp> checkIn(CheckInReq checkInReq) {
        return Rx2AndroidNetworking.post(ApiEndPoint.CHECK_IN)
                .addBodyParameter(checkInReq)
                .setOkHttpClient(okHttpClient)
                .build()
                .getObjectSingle(CheckInRsp.class);
    }

    @Override
    public Single<CheckOutRsp> checkOut(CheckOutReq checkOutReq) {
        return Rx2AndroidNetworking.post(ApiEndPoint.CHECK_OUT)
                .addBodyParameter(checkOutReq)
                .setOkHttpClient(okHttpClient)
                .build()
                .getObjectSingle(CheckOutRsp.class);
    }

    @Override
    public Single<GetLeaveRsp> getLeaveRequestList(GetLeavesReq getLeavesListReq) {
        return Rx2AndroidNetworking.post(ApiEndPoint.GET_LEAVE_LIST)
                .addBodyParameter(getLeavesListReq)
                .setOkHttpClient(okHttpClient)
                .build()
                .getObjectSingle(GetLeaveRsp.class);
    }

    @Override
    public Single<AddModifyLeaveRsp> addModifyLeave(AddModifyLeaveReq addModifyLeaveReq) throws JSONException {
        return Rx2AndroidNetworking.post(ApiEndPoint.ADD_MODIFY_LEAVE)
//                .addBodyParameter(addModifyLeaveReq)
                .addJSONObjectBody(new JSONObject(new Gson().toJson(addModifyLeaveReq)))
                .setOkHttpClient(okHttpClient)
                .build()
                .getObjectSingle(AddModifyLeaveRsp.class);
    }

    @Override
    public Single<LeaveTypeRsp> getLeaveType(LeaveTypeReq leaveTypeReq) {
        return Rx2AndroidNetworking.post(ApiEndPoint.GET_LEAVE_TYPE)
                .addBodyParameter(leaveTypeReq)
                .setOkHttpClient(okHttpClient)
                .build()
                .getObjectSingle(LeaveTypeRsp.class);
    }

    @Override
    public Single<AddPunchingSlipRsp> addPunchingSlip(AddPunchingSlipReq req) {
        return Rx2AndroidNetworking.post(ApiEndPoint.ADD_PUNCHING_SLIP)
                .addBodyParameter(req)
                .setOkHttpClient(okHttpClient)
                .build()
                .getObjectSingle(AddPunchingSlipRsp.class);
    }

    @Override
    public Single<MissPunchListRsp> getMissPunchList(MissPunchListReq req) {
        return Rx2AndroidNetworking.post(ApiEndPoint.GET_MISSPUNCH_SLIP)
                .addBodyParameter(req)
                .setOkHttpClient(okHttpClient)
                .build()
                .getObjectSingle(MissPunchListRsp.class);
    }

    @Override
    public Single<AddOverTimeRsp> addOvertime(AddOverTimeReq req) {
        return Rx2AndroidNetworking.post(ApiEndPoint.ADD_OVER_TIME)
                .addBodyParameter(req)
                .setOkHttpClient(okHttpClient)
                .build()
                .getObjectSingle(AddOverTimeRsp.class);
    }

    @Override
    public Single<OverTimeListRsp> getOvertimeList(OverTimeListReq req) {
        return Rx2AndroidNetworking.post(ApiEndPoint.GET_OVER_TIME_LIST)
                .addBodyParameter(req)
                .setOkHttpClient(okHttpClient)
                .build()
                .getObjectSingle(OverTimeListRsp.class);
    }

    @Override
    public Single<ShiftChangeRsp> shiftChangeRequest(ShiftChangeReq req) {
        return Rx2AndroidNetworking.post(ApiEndPoint.SHIFT_CHANGE_REQUEST)
                .addBodyParameter(req)
                .setOkHttpClient(okHttpClient)
                .build()
                .getObjectSingle(ShiftChangeRsp.class);
    }

    @Override
    public Single<ShiftChangeListRsp> getShiftChangeList(ShiftChangeListReq req) {
        return Rx2AndroidNetworking.post(ApiEndPoint.GET_SHIFT_CHANGE_REQUEST_LIST)
                .addBodyParameter(req)
                .setOkHttpClient(okHttpClient)
                .build()
                .getObjectSingle(ShiftChangeListRsp.class);
    }

    @Override
    public Single<AddCoRsp> addCo(AddCoReq req) {
        return Rx2AndroidNetworking.post(ApiEndPoint.ADD_CO)
                .addBodyParameter(req)
                .setOkHttpClient(okHttpClient)
                .build()
                .getObjectSingle(AddCoRsp.class);
    }

    @Override
    public Single<CoListRsp> getCoList(CoListReq req) {
        return Rx2AndroidNetworking.post(ApiEndPoint.GET_CO_LIST)
                .addBodyParameter(req)
                .setOkHttpClient(okHttpClient)
                .build()
                .getObjectSingle(CoListRsp.class);
    }
}
