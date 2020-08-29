package com.gipl.notifyme.data;

import android.content.Context;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.room.Room;

import com.gipl.notifyme.BuildConfig;
import com.gipl.notifyme.data.local.db.NotifyMeDatabase;
import com.gipl.notifyme.data.local.prefs.PreferencesHelper;
import com.gipl.notifyme.data.model.api.notification.GetNotificationRes;
import com.gipl.notifyme.data.model.api.notification.GetNotificationsReq;
import com.gipl.notifyme.data.model.api.notification.Notification;
import com.gipl.notifyme.data.model.api.sendotp.SendOTPReq;
import com.gipl.notifyme.data.model.api.sendotp.SendOtpRes;
import com.gipl.notifyme.data.model.api.sendotp.User;
import com.gipl.notifyme.data.model.api.verifyotp.VerifyOtpReq;
import com.gipl.notifyme.data.model.api.verifyotp.VerifyOtpRsp;
import com.gipl.notifyme.data.model.db.TNotification;
import com.gipl.notifyme.data.remote.ApiHelper;

import java.util.ArrayList;
import java.util.List;

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
    private NotifyMeDatabase mDatabase;

    @Inject
    public AppDataManager(Context context, PreferencesHelper preferencesHelper, ApiHelper apiHelper) {
        mContext = context;
        mPreferencesHelper = preferencesHelper;
        mApiHelper = apiHelper;
        mDatabase = Room.databaseBuilder(context, NotifyMeDatabase.class, BuildConfig.DB_NAME).build();
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
    public Single<GetNotificationRes> getNotifications(GetNotificationsReq req) {
        return mApiHelper.getNotifications(req);
    }

    @Override
    public boolean isLogin() {
        return mPreferencesHelper.isLogin();
    }

    @Override
    public void setIsLogin() {
        mPreferencesHelper.setIsLogin();
    }

    @Override
    public void setEmpCode(String empCode) {
        mPreferencesHelper.setEmpCode(empCode);
    }

    @Override
    public String getEmpCode() {
        return mPreferencesHelper.getEmpCode();
    }

    @Override
    public void setUserObj(User user) {
        mPreferencesHelper.setUserObj(user);
    }

    @Override
    public User getUserObj() {
        return mPreferencesHelper.getUserObj();
    }

    @Override
    public void setLastSync(String date) {
        mPreferencesHelper.setLastSync(date);
    }

    @Override
    public String getLastSync() {
        return mPreferencesHelper.getLastSync();
    }

    @Override
    public void setTotalNotificationCache(int count) {
        mPreferencesHelper.setTotalNotificationCache(count);
    }

    @Override
    public int getCacheNotificationCount() {
        return mPreferencesHelper.getCacheNotificationCount();
    }

    @Override
    public long insertNotification(TNotification tNotification) {
       return mDatabase.notificationCacheDao().insertNotification(tNotification);
    }

    @Override
    public LiveData<List<Notification>> getNotificationList() {
     return mDatabase.notificationCacheDao().getNotificationList();

    }

    @Override
    public int getTotalNotificationCacheRm() {
        return mDatabase.notificationCacheDao().getTotalNotificationCache();
    }
}
