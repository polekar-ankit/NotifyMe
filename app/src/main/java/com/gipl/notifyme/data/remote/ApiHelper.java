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

import com.gipl.notifyme.data.model.api.checkin.CheckInReq;
import com.gipl.notifyme.data.model.api.checkin.CheckInRsp;
import com.gipl.notifyme.data.model.api.checkout.CheckOutReq;
import com.gipl.notifyme.data.model.api.checkout.CheckOutRsp;
import com.gipl.notifyme.data.model.api.lib.GetLibReq;
import com.gipl.notifyme.data.model.api.lib.GetLibRes;
import com.gipl.notifyme.data.model.api.notification.GetNotificationRes;
import com.gipl.notifyme.data.model.api.notification.GetNotificationsReq;
import com.gipl.notifyme.data.model.api.sendotp.SendOTPReq;
import com.gipl.notifyme.data.model.api.sendotp.SendOtpRes;
import com.gipl.notifyme.data.model.api.verifyotp.VerifyOtpReq;
import com.gipl.notifyme.data.model.api.verifyotp.VerifyOtpRsp;

import io.reactivex.Single;

/**
 * Created by amitshekhar on 07/07/17.
 */

public interface ApiHelper {
    Single<SendOtpRes> sendOtp(SendOTPReq sendOTPReq);

    Single<VerifyOtpRsp> verifyOtp(VerifyOtpReq verifyOtpReq);

    Single<GetLibRes> getLib(GetLibReq getLibReq);

    Single<GetNotificationRes> getNotifications(GetNotificationsReq req);

    Single<CheckInRsp> checkIn(CheckInReq checkInReq);

    Single<CheckOutRsp> checkOut(CheckOutReq checkOutReq);
}
