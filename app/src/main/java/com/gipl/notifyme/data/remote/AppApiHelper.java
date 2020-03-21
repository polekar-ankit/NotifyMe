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


import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.gipl.notifyme.data.model.api.notification.GetNotificationRes;
import com.gipl.notifyme.data.model.api.notification.GetNotificationsReq;
import com.gipl.notifyme.data.model.api.sendotp.SendOTPReq;
import com.gipl.notifyme.data.model.api.sendotp.SendOtpRes;
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


    private HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    private OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .addInterceptor(httpLoggingInterceptor)
            .build();


    @Inject
    public AppApiHelper() {
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
    public Single<GetNotificationRes> getNotifications(GetNotificationsReq req) {
        return Rx2AndroidNetworking.post(ApiEndPoint.GET_NOTIFICATION)
                .addBodyParameter(req)
                .setOkHttpClient(okHttpClient)
                .build()
                .getObjectSingle(GetNotificationRes.class);
    }
}
