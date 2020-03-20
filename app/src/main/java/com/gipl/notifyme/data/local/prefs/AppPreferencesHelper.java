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

    private static final String PREF_KEY_SESSION_ID = "PREF_SESSION_ID";


    private final SharedPreferences mPrefs;

    @Inject
    public AppPreferencesHelper(Context context, @PreferenceInfo String prefFileName) {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }


    @Override
    public String getSessionId() {
        return mPrefs.getString(PREF_KEY_SESSION_ID, "");
    }

    @Override
    public void setSessionId(String sessionId) {
        mPrefs.edit().putString(PREF_KEY_SESSION_ID, sessionId).apply();
    }

}
