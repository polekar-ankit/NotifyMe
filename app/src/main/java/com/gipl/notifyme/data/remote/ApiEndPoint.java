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
    static final String GET_LIB = BuildConfig.BASE_URL + BuildConfig.NOYIFY_ME + "/Auth/GetLib";
    static final String CHECK_IN = BuildConfig.BASE_URL + BuildConfig.NOYIFY_ME + "/Attendance/CheckIn";
    static final String CHECK_OUT = BuildConfig.BASE_URL + BuildConfig.NOYIFY_ME + "/Attendance/CheckOut";
    static final String ADD_MODIFY_LEAVE = BuildConfig.BASE_URL + BuildConfig.NOYIFY_ME + "/Leave/AddOrModifyLeaveRequest";
    static final String GET_LEAVE_TYPE = BuildConfig.BASE_URL + BuildConfig.NOYIFY_ME + "/Leave/GetLeaveAprovalsList";
    static final String GET_NOTIFICATION = BuildConfig.BASE_URL + BuildConfig.NOYIFY_ME + "/Notification/GetAllNotifications";
    static final String GET_LEAVE_LIST = BuildConfig.BASE_URL + BuildConfig.NOYIFY_ME + "/Leave/GetLeaveRequestsList";
    static final String ADD_PUNCHING_SLIP = BuildConfig.BASE_URL + BuildConfig.NOYIFY_ME + "/MissPunch/AddOrModifyMissPunch";
    static final String GET_MISSPUNCH_SLIP = BuildConfig.BASE_URL + BuildConfig.NOYIFY_ME + "/MissPunch/GetMissPunchList";
    static final String ADD_OVER_TIME = BuildConfig.BASE_URL + BuildConfig.NOYIFY_ME + "/OverTime/AddOrModifyOverTime";
    static final String GET_OVER_TIME_LIST = BuildConfig.BASE_URL + BuildConfig.NOYIFY_ME + "/OverTime/GetOverTimeList";
    static final String SHIFT_CHANGE_REQUEST = BuildConfig.BASE_URL + BuildConfig.NOYIFY_ME + "/ShiftChangeReq/AddOrModifyShiftChangeReq";
    static final String GET_SHIFT_CHANGE_REQUEST_LIST = BuildConfig.BASE_URL + BuildConfig.NOYIFY_ME + "/ShiftChangeReq/GetAllShiftChangeReqs";
    static final String ADD_CO = BuildConfig.BASE_URL + BuildConfig.NOYIFY_ME + "/COReq/AddOrModifyCO";
    static final String GET_CO_LIST = BuildConfig.BASE_URL + BuildConfig.NOYIFY_ME + "/COReq/GetAllCOReqList";
    static final String GET_LEAVE_BALANCE = BuildConfig.BASE_URL + BuildConfig.NOYIFY_ME + "/LeaveBalance/GetAllLeaveBalanceByUser";
    static final String GET_SHIFTS_FOR_USER = BuildConfig.BASE_URL + BuildConfig.NOYIFY_ME + "/ShiftChangeReq/GetShiftsForUser";
    static final String GET_DASHBOARD_COUNT = BuildConfig.BASE_URL + BuildConfig.NOYIFY_ME + "/User/GetMobileDashboardCount";

    private ApiEndPoint() {
        // This class is not publicly instantiable
    }
}
