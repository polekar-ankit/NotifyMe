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


import com.gipl.notifyme.BuildConfig;

/**
 * Created by amitshekhar on 07/07/17.
 */

public final class ApiEndPoint {

    //    define api names
//    static final String FIND_USERS = BuildConfig.BASE_URL + "/User/FindUsers";
    static final String SEND_OTP = BuildConfig.BASE_URL + BuildConfig.NOYIFY_ME + "/Auth/SendOTP";
    static final String VERIFY_OTP = BuildConfig.BASE_URL + BuildConfig.NOYIFY_ME + "/Auth/Login";
    static final String GET_NOTIFICATION = BuildConfig.BASE_URL + BuildConfig.NOYIFY_ME + "/Notification/GetAllNotifications";

    private ApiEndPoint() {
        // This class is not publicly instantiable
    }
}
