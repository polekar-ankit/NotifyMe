package com.gipl.notifyme.data;

import android.content.Context;

import com.gipl.notifyme.data.local.prefs.PreferencesHelper;
import com.gipl.notifyme.data.model.api.sendotp.SendOTPReq;
import com.gipl.notifyme.data.model.api.sendotp.SendOtpRes;
import com.gipl.notifyme.data.remote.ApiHelper;

import org.json.JSONException;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;


/**
 * Created by amitshekhar on 07/07/17.
 */
@Singleton
public class AppDataManager implements DataManager {

    private final ApiHelper mApiHelper;

    private final Context mContext;


    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public AppDataManager(Context context, PreferencesHelper preferencesHelper, ApiHelper apiHelper) {
        mContext = context;
        mPreferencesHelper = preferencesHelper;
        mApiHelper = apiHelper;
    }


    @Override
    public Context getContext() {
        return mContext;
    }


    @Override
    public String getSessionId() {
        return null;
    }

    @Override
    public void setSessionId(String sessionId) {
    }

    @Override
    public Single<SendOtpRes> sendOtp(SendOTPReq sendOTPReq) throws JSONException {
        return mApiHelper.sendOtp(sendOTPReq);
    }
}
