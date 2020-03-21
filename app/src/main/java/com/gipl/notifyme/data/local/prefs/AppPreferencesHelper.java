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

import com.gipl.notifyme.di.PreferenceInfo;

import javax.inject.Inject;

/**
 * Created by amitshekhar on 07/07/17.
 */

public class AppPreferencesHelper implements PreferencesHelper {

    private static final String KEY_IS_LOGIN = "KEY_IS_LOGIN";
    private static final String KEY_EMP_CODE = "KEY_EMP_CODE";
    private static final String KEY_LAST_SYNC = "KEY_LAST_SYNC";


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
    public void setLastSync(String date) {
        mPrefs.edit().putString(KEY_LAST_SYNC,date).apply();
    }

    @Override
    public String getLastSync() {
        return mPrefs.getString(KEY_LAST_SYNC,"Not Available");
    }
}
