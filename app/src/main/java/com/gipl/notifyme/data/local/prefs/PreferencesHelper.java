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

package com.gipl.notifyme.data.local.prefs;


import com.gipl.notifyme.data.model.api.dashbordcount.DashboardCountRsp;
import com.gipl.notifyme.data.model.api.leavebalance.LeaveBalance;
import com.gipl.notifyme.data.model.api.leavebalance.LeaveBalanceRsp;
import com.gipl.notifyme.data.model.api.leavetype.LeaveTypeRsp;
import com.gipl.notifyme.data.model.api.lib.Shifts;
import com.gipl.notifyme.data.model.api.lib.Utility;
import com.gipl.notifyme.data.model.api.sendotp.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amitshekhar on 07/07/17.
 */

public interface PreferencesHelper {
    boolean isLogin();

    void setIsLogin();

    void logout();

    String getEmpCode();

    void setEmpCode(String empCode);

    String getSession();

    //user data
    void setSession(String session);

    User getUserObj();

    void setUserObj(User user);

    String getLastSync();

    void setLastSync(String date);

    //shift data

    String getActiveShift();

    void setActiveShift(String suidShift);

    long getCheckInTime();

    void setCheckInTime(long checkInTime);

    int getCheckType();

    void setCheckType(int checkType);

    //nofication cache
    void setTotalNotificationCache(int count);

    int getCacheNotificationCount();

    Utility getUtility();

    // get Lib call
    void setUtility(Utility utility);

    List<Shifts> getShiftList();

    void setShiftList(List<Shifts> shiftsList);

    LeaveTypeRsp getCacheLeaveType();

    void setCacheLeaveType(LeaveTypeRsp leaveType);

    //reason cache
    void setReasonCacheDate(String type, long lastSyncDate);

    long getReasonCacheDate(String type);

    void setLanguageCode(String code);

    String getLanguageCode();

    void cacheLeaveBalance(LeaveBalanceRsp leaveBalanceRsp);

    ArrayList<LeaveBalance> getLeaveBalance();

    void setDashboardCount(DashboardCountRsp rsp);

    DashboardCountRsp getDashboardCount();

}
