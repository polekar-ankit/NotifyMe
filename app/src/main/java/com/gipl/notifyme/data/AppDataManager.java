package com.gipl.notifyme.data;

import android.content.Context;

import com.gipl.notifyme.data.local.prefs.PreferencesHelper;
import com.gipl.notifyme.data.model.api.sendotp.SendOTPReq;
import com.gipl.notifyme.data.model.api.sendotp.SendOtpRes;
import com.gipl.notifyme.data.model.api.sendotp.User;
import com.gipl.notifyme.data.model.api.verifyotp.VerifyOtpReq;
import com.gipl.notifyme.data.model.api.verifyotp.VerifyOtpRsp;
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
    public Single<SendOtpRes> sendOtp(SendOTPReq sendOTPReq) {
        return mApiHelper.sendOtp(sendOTPReq);
    }

    @Override
    public Single<VerifyOtpRsp> verifyOtp(VerifyOtpReq verifyOtpReq) {
        return mApiHelper.verifyOtp(verifyOtpReq);
    }

    @Override
    public boolean isLogin() {
        return mPreferencesHelper.isLogin();
    }

    @Override
    public void setIsLogin() {
        mPreferencesHelper.setIsLogin();
    }
}
