package com.gipl.notifyme.data;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.gipl.notifyme.BuildConfig;
import com.gipl.notifyme.data.local.db.NotifyMeDatabase;
import com.gipl.notifyme.data.local.prefs.PreferencesHelper;
import com.gipl.notifyme.data.model.api.addco.AddCoReq;
import com.gipl.notifyme.data.model.api.addco.AddCoRsp;
import com.gipl.notifyme.data.model.api.addovertime.AddOverTimeReq;
import com.gipl.notifyme.data.model.api.addovertime.AddOverTimeRsp;
import com.gipl.notifyme.data.model.api.applyleave.AddModifyLeaveReq;
import com.gipl.notifyme.data.model.api.applyleave.AddModifyLeaveRsp;
import com.gipl.notifyme.data.model.api.checkin.CheckInReq;
import com.gipl.notifyme.data.model.api.checkin.CheckInRsp;
import com.gipl.notifyme.data.model.api.checkout.CheckOutReq;
import com.gipl.notifyme.data.model.api.checkout.CheckOutRsp;
import com.gipl.notifyme.data.model.api.colist.CoListReq;
import com.gipl.notifyme.data.model.api.colist.CoListRsp;
import com.gipl.notifyme.data.model.api.leaves.GetLeaveRsp;
import com.gipl.notifyme.data.model.api.leaves.GetLeavesReq;
import com.gipl.notifyme.data.model.api.leavetype.LeaveTypeReq;
import com.gipl.notifyme.data.model.api.leavetype.LeaveTypeRsp;
import com.gipl.notifyme.data.model.api.lib.GetLibReq;
import com.gipl.notifyme.data.model.api.lib.GetLibRes;
import com.gipl.notifyme.data.model.api.lib.Shifts;
import com.gipl.notifyme.data.model.api.lib.Utility;
import com.gipl.notifyme.data.model.api.mispunchlist.MissPunchListReq;
import com.gipl.notifyme.data.model.api.mispunchlist.MissPunchListRsp;
import com.gipl.notifyme.data.model.api.notification.GetNotificationRes;
import com.gipl.notifyme.data.model.api.notification.GetNotificationsReq;
import com.gipl.notifyme.data.model.api.notification.Notification;
import com.gipl.notifyme.data.model.api.overtimelist.OverTimeListReq;
import com.gipl.notifyme.data.model.api.overtimelist.OverTimeListRsp;
import com.gipl.notifyme.data.model.api.punchingslip.AddPunchingSlipReq;
import com.gipl.notifyme.data.model.api.punchingslip.AddPunchingSlipRsp;
import com.gipl.notifyme.data.model.api.sendotp.SendOTPReq;
import com.gipl.notifyme.data.model.api.sendotp.SendOtpRes;
import com.gipl.notifyme.data.model.api.sendotp.User;
import com.gipl.notifyme.data.model.api.shiftchange.ShiftChangeReq;
import com.gipl.notifyme.data.model.api.shiftchange.ShiftChangeRsp;
import com.gipl.notifyme.data.model.api.shiftchangelist.ShiftChangeListReq;
import com.gipl.notifyme.data.model.api.shiftchangelist.ShiftChangeListRsp;
import com.gipl.notifyme.data.model.api.verifyotp.VerifyOtpReq;
import com.gipl.notifyme.data.model.api.verifyotp.VerifyOtpRsp;
import com.gipl.notifyme.data.model.db.TNotification;
import com.gipl.notifyme.data.model.db.TReason;
import com.gipl.notifyme.data.remote.ApiHelper;
import com.gipl.notifyme.ui.model.Reason;

import org.json.JSONException;

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
        mDatabase = Room.databaseBuilder(context, NotifyMeDatabase.class, BuildConfig.DB_NAME)
                .build();
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
    public Single<GetLibRes> getLib(GetLibReq getLibReq) {
        return mApiHelper.getLib(getLibReq);
    }

    @Override
    public Single<GetNotificationRes> getNotifications(GetNotificationsReq req) {
        return mApiHelper.getNotifications(req);
    }

    @Override
    public Single<CheckInRsp> checkIn(CheckInReq checkInReq) {
        return mApiHelper.checkIn(checkInReq);
    }

    @Override
    public Single<CheckOutRsp> checkOut(CheckOutReq checkOutReq) {
        return mApiHelper.checkOut(checkOutReq);
    }

    @Override
    public Single<GetLeaveRsp> getLeaveRequestList(GetLeavesReq getLeavesListReq) {
        return mApiHelper.getLeaveRequestList(getLeavesListReq);
    }

    @Override
    public Single<AddModifyLeaveRsp> addModifyLeave(AddModifyLeaveReq addModifyLeaveReq) throws JSONException {
        return mApiHelper.addModifyLeave(addModifyLeaveReq);
    }

    @Override
    public Single<LeaveTypeRsp> getLeaveType(LeaveTypeReq leaveTypeReq) {
        return mApiHelper.getLeaveType(leaveTypeReq);
    }

    @Override
    public Single<AddPunchingSlipRsp> addPunchingSlip(AddPunchingSlipReq req) {
        return mApiHelper.addPunchingSlip(req);
    }

    @Override
    public Single<MissPunchListRsp> getMissPunchList(MissPunchListReq req) {
        return mApiHelper.getMissPunchList(req);
    }

    @Override
    public Single<AddOverTimeRsp> addOvertime(AddOverTimeReq addOverTimeReq) {
        return mApiHelper.addOvertime(addOverTimeReq);
    }

    @Override
    public Single<OverTimeListRsp> getOvertimeList(OverTimeListReq req) {
        return mApiHelper.getOvertimeList(req);
    }

    @Override
    public Single<ShiftChangeRsp> shiftChangeRequest(ShiftChangeReq req) {
        return mApiHelper.shiftChangeRequest(req);
    }

    @Override
    public Single<ShiftChangeListRsp> getShiftChangeList(ShiftChangeListReq req) {
        return mApiHelper.getShiftChangeList(req);
    }

    @Override
    public Single<AddCoRsp> addCo(AddCoReq req) {
        return mApiHelper.addCo(req);
    }

    @Override
    public Single<CoListRsp> getCoList(CoListReq req) {
        return mApiHelper.getCoList(req);
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
    public void setSession(String session) {
        mPreferencesHelper.setSession(session);
    }

    @Override
    public String getSession() {
        return mPreferencesHelper.getSession();
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
    public String getActiveShift() {
        return mPreferencesHelper.getActiveShift();
    }

    @Override
    public void setActiveShift(String suidShift) {
        mPreferencesHelper.setActiveShift(suidShift);
    }

    @Override
    public long getCheckInTime() {
        return mPreferencesHelper.getCheckInTime();
    }

    @Override
    public void setCheckInTime(long checkInTime) {
        mPreferencesHelper.setCheckInTime(checkInTime);
    }

    @Override
    public int getCheckType() {
        return mPreferencesHelper.getCheckType();
    }

    @Override
    public void setCheckType(int checkType) {
        mPreferencesHelper.setCheckType(checkType);
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
    public Utility getUtility() {
        return mPreferencesHelper.getUtility();
    }

    @Override
    public void setUtility(Utility utility) {
        mPreferencesHelper.setUtility(utility);
    }

    @Override
    public void setShiftList(List<Shifts> shiftsList) {
        mPreferencesHelper.setShiftList(shiftsList);
    }

    @Override
    public LeaveTypeRsp getCacheLeaveType() {
        return mPreferencesHelper.getCacheLeaveType();
    }

    @Override
    public void setCacheLeaveType(LeaveTypeRsp leaveType) {
        mPreferencesHelper.setCacheLeaveType(leaveType);
    }

    @Override
    public void setReasonCacheDate(String type, long lastSyncDate) {
        mPreferencesHelper.setReasonCacheDate(type, lastSyncDate);
    }

    @Override
    public long getReasonCacheDate(String type) {
        return mPreferencesHelper.getReasonCacheDate(type);
    }

    @Override
    public List<Shifts> getShiftList() {
        return mPreferencesHelper.getShiftList();
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

    @Override
    public long insertReason(TReason tReason) {
        return mDatabase.reasonCacheDao().insertReason(tReason);
    }

    @Override
    public LiveData<List<Reason>> getReasonList(String type) {
        return mDatabase.reasonCacheDao().getReasonList(type);
    }

    @Override
    public int clear(String type) {
        return mDatabase.reasonCacheDao().clear(type);
    }
}
