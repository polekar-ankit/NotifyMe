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

import android.content.Context;
import android.content.SharedPreferences;

import com.gipl.notifyme.data.model.api.lib.Shifts;
import com.gipl.notifyme.data.model.api.lib.Utility;
import com.gipl.notifyme.data.model.api.sendotp.User;
import com.gipl.notifyme.di.PreferenceInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by amitshekhar on 07/07/17.
 */

public class AppPreferencesHelper implements PreferencesHelper {

    private static final String KEY_IS_LOGIN = "KEY_IS_LOGIN";
    private static final String KEY_EMP_CODE = "KEY_EMP_CODE";
    private static final String KEY_LAST_SYNC = "KEY_LAST_SYNC";
    private static final String KEY_USER_OBJ = "KEY_USER_OBJ";
    private static final String KEY_CACHE_NOTIFICATION = "KEY_CACHE_NOTIFICATION";
    private static final String KEY_SHIFT_DATA = "KEY_SHIFT_DATA";
    private static final String KEY_UTILITY_LIB = "KEY_UTILITY_LIB";
    private static final String KEY_SESSION = "KEY_SESSION";
    private static final String KEY_ACTIVE_SHIFT_SUID = "KEY_SHIFT_SUID";
    private static final String KEY_CHECK_IN_TIME = "KEY_CHECK_IN_TIME";
    private static final String KEY_CHECK_TYPE = "KEY_CHECK_TYPE";


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
    public void setEmpCode(String empCode) {
        mPrefs.edit().putString(KEY_EMP_CODE, empCode).apply();
    }

    @Override
    public String getEmpCode() {
        return mPrefs.getString(KEY_EMP_CODE, "");
    }

    @Override
    public void setSession(String session) {
        mPrefs.edit().putString(KEY_SESSION, session).apply();
    }

    @Override
    public String getSession() {
        return mPrefs.getString(KEY_SESSION, "");
    }

    @Override
    public void setActiveShift(String suidShift) {
        mPrefs.edit().putString(KEY_ACTIVE_SHIFT_SUID, suidShift).apply();
    }

    @Override
    public String getActiveShift() {
        return mPrefs.getString(KEY_ACTIVE_SHIFT_SUID, "");
    }

    @Override
    public void setCheckInTime(long checkInTime) {
        mPrefs.edit().putLong(KEY_CHECK_IN_TIME, checkInTime).apply();
    }

    @Override
    public int getCheckType() {
        return mPrefs.getInt(KEY_CHECK_TYPE,2);
    }

    @Override
    public void setCheckType(int checkType) {
        mPrefs.edit().putInt(KEY_CHECK_TYPE,checkType).apply();
    }

    @Override
    public long getCheckInTime() {
        return mPrefs.getLong(KEY_CHECK_IN_TIME, 0);
    }

    @Override
    public void setUserObj(User user) {
        mPrefs.edit().putString(KEY_USER_OBJ, new Gson().toJson(user)).apply();
    }

    @Override
    public User getUserObj() {
        String user = mPrefs.getString(KEY_USER_OBJ, "");
        return user.isEmpty() ? null : new Gson().fromJson(user, User.class);
    }

    @Override
    public void setLastSync(String date) {
        mPrefs.edit().putString(KEY_LAST_SYNC, date).apply();
    }

    @Override
    public String getLastSync() {
        return mPrefs.getString(KEY_LAST_SYNC, "Not Available");
    }

    @Override
    public void setTotalNotificationCache(int count) {
        mPrefs.edit().putInt(KEY_CACHE_NOTIFICATION, count).apply();
    }

    @Override
    public int getCacheNotificationCount() {
        return mPrefs.getInt(KEY_CACHE_NOTIFICATION, 0);
    }

    @Override
    public void setUtility(Utility utility) {
        mPrefs.edit().putString(KEY_UTILITY_LIB, new Gson().toJson(utility)).apply();
    }

    @Override
    public Utility getUtility() {
        String json = mPrefs.getString(KEY_UTILITY_LIB, "");
        return json.isEmpty() ? null : new Gson().fromJson(json, Utility.class);
    }

    @Override
    public void setShiftList(List<Shifts> shiftsList) {
        mPrefs.edit().putString(KEY_SHIFT_DATA, new Gson().toJson(shiftsList)).apply();
    }

    @Override
    public List<Shifts> getShiftList() {
        String json = mPrefs.getString(KEY_SHIFT_DATA, "");
        if (json.isEmpty())
            return new ArrayList<>();
        return new Gson().fromJson(json, new TypeToken<List<Shifts>>() {
        }.getType());
    }
}
