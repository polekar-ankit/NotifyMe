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

package com.gipl.swayam.data.local.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.gipl.swayam.data.model.api.dashbordcount.DashboardCountRsp;
import com.gipl.swayam.data.model.api.leavebalance.LeaveBalance;
import com.gipl.swayam.data.model.api.leavebalance.LeaveBalanceRsp;
import com.gipl.swayam.data.model.api.leavetype.LeaveTypeRsp;
import com.gipl.swayam.data.model.api.lib.Shifts;
import com.gipl.swayam.data.model.api.lib.Utility;
import com.gipl.swayam.data.model.api.sendotp.User;
import com.gipl.swayam.di.PreferenceInfo;
import com.gipl.swayam.ui.changelng.ChangeLanguageFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by amitshekhar on 07/07/17.
 */

public class AppPreferencesHelper implements PreferencesHelper {

    public static final String KEY_LANG_CODE = "KEY_LANG_CODE";
    private static final String KEY_IS_LOGIN = "KEY_IS_LOGIN";
    private static final String KEY_EMP_CODE = "KEY_EMP_CODE";
    private static final String KEY_LAST_NOTIFICATION_SYNC = "KEY_LAST_SYNC";
    private static final String KEY_USER_OBJ = "KEY_USER_OBJ";
    private static final String KEY_NUM_CACHE_NOTIFICATION = "KEY_CACHE_NOTIFICATION";
    private static final String KEY_SHIFT_DATA = "KEY_SHIFT_DATA";
    private static final String KEY_UTILITY_LIB = "KEY_UTILITY_LIB";
    private static final String KEY_SESSION = "KEY_SESSION";
    private static final String KEY_ACTIVE_SHIFT_SUID = "KEY_SHIFT_SUID";
    private static final String KEY_CHECK_IN_TIME = "KEY_CHECK_IN_TIME";
    private static final String KEY_CHECK_TYPE = "KEY_CHECK_TYPE";
    private static final String KEY_CACHE_lEAVAE_TYPE = "KEY_CACHE_LEAVE_TYPE";
    private static final String KEY_CACHE_lEAVE_BALANCE = "KEY_CACHE_lEAVE_BALANCE";
    private static final String KEY_CACHE_DASHBOARD_COUNT = "KEY_CACHE_DASHBOARD_COUNT";


    private final SharedPreferences mPrefs;

    @Inject
    public AppPreferencesHelper(Context context, @PreferenceInfo String prefFileName) {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }


    @Override
    public boolean isLogin() {
        return mPrefs.getBoolean(KEY_IS_LOGIN, false);
    }

    @Override
    public void setIsLogin() {
        mPrefs.edit().putBoolean(KEY_IS_LOGIN, true).apply();
    }

    @Override
    public void logout() {
        mPrefs.edit().remove(KEY_IS_LOGIN).apply();
        mPrefs.edit().remove(KEY_SESSION).apply();
        mPrefs.edit().remove(KEY_EMP_CODE).apply();
        mPrefs.edit().remove(KEY_USER_OBJ).apply();
        mPrefs.edit().remove(KEY_LAST_NOTIFICATION_SYNC).apply();
        mPrefs.edit().remove(KEY_NUM_CACHE_NOTIFICATION).apply();
        mPrefs.edit().remove(KEY_SHIFT_DATA).apply();
        mPrefs.edit().remove(KEY_ACTIVE_SHIFT_SUID).apply();
        mPrefs.edit().remove(KEY_CACHE_lEAVE_BALANCE).apply();
    }

    @Override
    public String getEmpCode() {
        return mPrefs.getString(KEY_EMP_CODE, "");
    }

    @Override
    public void setEmpCode(String empCode) {
        mPrefs.edit().putString(KEY_EMP_CODE, empCode).apply();
    }

    @Override
    public String getSession() {
        return mPrefs.getString(KEY_SESSION, "");
    }

    @Override
    public void setSession(String session) {
        mPrefs.edit().putString(KEY_SESSION, session).apply();
    }

    @Override
    public String getActiveShift() {
        return mPrefs.getString(KEY_ACTIVE_SHIFT_SUID, "");
    }

    @Override
    public void setActiveShift(String suidShift) {
        mPrefs.edit().putString(KEY_ACTIVE_SHIFT_SUID, suidShift).apply();
    }

    @Override
    public int getCheckType() {
        return mPrefs.getInt(KEY_CHECK_TYPE, 2);
    }

    @Override
    public void setCheckType(int checkType) {
        mPrefs.edit().putInt(KEY_CHECK_TYPE, checkType).apply();
    }

    @Override
    public long getCheckInTime() {
        return mPrefs.getLong(KEY_CHECK_IN_TIME, 0);
    }

    @Override
    public void setCheckInTime(long checkInTime) {
        mPrefs.edit().putLong(KEY_CHECK_IN_TIME, checkInTime).apply();
    }

    @Override
    public User getUserObj() {
        String user = mPrefs.getString(KEY_USER_OBJ, "");
        return user.isEmpty() ? null : new Gson().fromJson(user, User.class);
    }

    @Override
    public void setUserObj(User user) {
        mPrefs.edit().putString(KEY_USER_OBJ, new Gson().toJson(user)).apply();
    }

    @Override
    public String getLastSync() {
        return mPrefs.getString(KEY_LAST_NOTIFICATION_SYNC, "Not Available");
    }

    @Override
    public void setLastSync(String date) {
        mPrefs.edit().putString(KEY_LAST_NOTIFICATION_SYNC, date).apply();
    }

    @Override
    public void setTotalNotificationCache(int count) {
        mPrefs.edit().putInt(KEY_NUM_CACHE_NOTIFICATION, count).apply();
    }

    @Override
    public int getCacheNotificationCount() {
        return mPrefs.getInt(KEY_NUM_CACHE_NOTIFICATION, 0);
    }

    @Override
    public Utility getUtility() {
        String json = mPrefs.getString(KEY_UTILITY_LIB, "");
        return json.isEmpty() ? null : new Gson().fromJson(json, Utility.class);
    }

    @Override
    public void setUtility(Utility utility) {
        mPrefs.edit().putString(KEY_UTILITY_LIB, new Gson().toJson(utility)).apply();
    }

    @Override
    public LeaveTypeRsp getCacheLeaveType() {
        String json = mPrefs.getString(Base64.encodeToString(KEY_CACHE_lEAVAE_TYPE.getBytes(), Base64.DEFAULT), "");
        if (json.isEmpty()) {
            return null;
        }
        return new Gson().fromJson(json, LeaveTypeRsp.class);
    }

    @Override
    public void setCacheLeaveType(LeaveTypeRsp leaveType) {
        mPrefs.edit().putString(Base64.encodeToString(KEY_CACHE_lEAVAE_TYPE.getBytes(), Base64.DEFAULT), new Gson().toJson(leaveType)).apply();
    }

    @Override
    public void setReasonCacheDate(String type, long lastSyncDate) {
        mPrefs.edit().putLong(type, lastSyncDate).apply();
    }

    @Override
    public long getReasonCacheDate(String type) {
        return mPrefs.getLong(type, 0);
    }

    @Override
    public void setLanguageCode(String code) {
        mPrefs.edit().putString(KEY_LANG_CODE, code).apply();
    }

    @Override
    public String getLanguageCode() {
        return mPrefs.getString(KEY_LANG_CODE, ChangeLanguageFragment.englishCode);
    }

    @Override
    public void cacheLeaveBalance(LeaveBalanceRsp leaveBalanceRsp) {
        mPrefs.edit().putString(KEY_CACHE_lEAVE_BALANCE, new Gson().toJson(leaveBalanceRsp)).apply();
    }

    @Override
    public ArrayList<LeaveBalance> getLeaveBalance() {
        String json = mPrefs.getString(KEY_CACHE_lEAVE_BALANCE, "");
        if (json.isEmpty())
            return new ArrayList<>();
        LeaveBalanceRsp rsp = new Gson().fromJson(json, LeaveBalanceRsp.class);
        return rsp.getBalanceArrayList();
    }

    @Override
    public void setDashboardCount(DashboardCountRsp rsp) {
        mPrefs.edit().putString(KEY_CACHE_DASHBOARD_COUNT, new Gson().toJson(rsp)).apply();
    }

    @Override
    public DashboardCountRsp getDashboardCount() {
        String json = mPrefs.getString(KEY_CACHE_DASHBOARD_COUNT, "");
        if (json.isEmpty())
            return null;
        return new Gson().fromJson(json, DashboardCountRsp.class);
    }

    @Override
    public List<Shifts> getShiftList() {
        String json = mPrefs.getString(KEY_SHIFT_DATA, "");
        if (json.isEmpty())
            return new ArrayList<>();
        return new Gson().fromJson(json, new TypeToken<List<Shifts>>() {
        }.getType());
    }

    @Override
    public void setShiftList(List<Shifts> shiftsList) {
        mPrefs.edit().putString(KEY_SHIFT_DATA, new Gson().toJson(shiftsList)).apply();
    }


}
