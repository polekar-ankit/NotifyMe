package com.gipl.notifyme.domain;

import android.graphics.Color;

import com.gipl.notifyme.BuildConfig;
import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.data.model.api.applyleave.AddModifyLeaveReq;
import com.gipl.notifyme.data.model.api.applyleave.AddModifyLeaveRsp;
import com.gipl.notifyme.data.model.api.leaves.GetLeaveRsp;
import com.gipl.notifyme.data.model.api.leaves.GetLeavesReq;
import com.gipl.notifyme.data.model.api.leaves.LeaveRequest;
import com.gipl.notifyme.data.model.api.leavetype.LeaveTypeReq;
import com.gipl.notifyme.data.model.api.leavetype.LeaveTypeRsp;
import com.gipl.notifyme.data.model.api.lib.utility.LeaveStatus;
import com.gipl.notifyme.uility.TimeUtility;

import org.json.JSONException;

import java.sql.Time;
import java.text.ParseException;
import java.util.Calendar;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public class LeaveDomain extends UseCase {
    public LeaveDomain(DataManager dataManager) {
        super(dataManager);
    }

    public Single<GetLeaveRsp> getLeaveList(GetLeavesReq getLeavesReq) {
        return dataManager.getLeaveRequestList(getLeavesReq).map(getLeaveRsp -> {
            if (getLeaveRsp.getLeaveRequestArrayList() != null && getLeaveRsp.getLeaveRequestArrayList().size() > 0) {
                LeaveStatus leaveStatus = dataManager.getUtility().getLeaveStatus();
                for (LeaveRequest leaveRequest :
                        getLeaveRsp.getLeaveRequestArrayList()) {
                    if (leaveRequest.getLeaveStatus() == leaveStatus.getBitApproved()) {
                        leaveRequest.setColor(Color.parseColor("#43A047"));
                    } else if (leaveRequest.getLeaveStatus() == leaveStatus.getBitRejected() || leaveRequest.getLeaveStatus() == leaveStatus.getBitCancelled()) {
                        leaveRequest.setColor(Color.parseColor("#E53935"));
                    } else if (leaveRequest.getLeaveStatus() == leaveStatus.getBitPending()) {
                        leaveRequest.setColor(Color.parseColor("#FB8C00"));
                    }
                }
            }
            return getLeaveRsp;
        });
    }

    public Single<AddModifyLeaveRsp> addModifyLeave(AddModifyLeaveReq addModifyLeaveReq) throws JSONException {
        addModifyLeaveReq.setSuidSession(dataManager.getSession());
        addModifyLeaveReq.setSuidPlant(dataManager.getUserObj().getSuidPlant());
//        if (BuildConfig.DEBUG)
//            addModifyLeaveReq.setSuidUser("bed4167a404add6345172c0a47f923fdb9cc15aedbcb04fca4a9c5cd3c42e7b9");
//        else
        addModifyLeaveReq.setSuidUser(dataManager.getUserObj().getSuidUser());
        addModifyLeaveReq.setsEmpCode(dataManager.getEmpCode());
        return dataManager.addModifyLeave(addModifyLeaveReq);
    }

    /*here we are store leave type in cache
     *after every one day we are requesting for refresh list
     * @return
     */
    public Single<LeaveTypeRsp> getLeaveType() {
        LeaveTypeRsp leaveTypeRsp = dataManager.getCacheLeaveType();
        boolean refreshCache = true;
        if (leaveTypeRsp != null) {
            try {
                //we are passing current utc time for in tag so we are using tag for days calculation
                long diff = TimeUtility.getDiff(TimeUtility.convertUtcTimeToLong(leaveTypeRsp.getTag()));
                long days = diff / (1000 * 60 * 60 * 24);
                refreshCache = days > 1;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (refreshCache) {
            LeaveTypeReq leaveTypeReq = new LeaveTypeReq();
            leaveTypeReq.setSuidSession(dataManager.getSession());
            leaveTypeReq.setPagination(false);
            leaveTypeReq.setTag(TimeUtility.getCurrentUtcDateTimeForApi());
            return dataManager.getLeaveType(leaveTypeReq).map(leaveTypeRsp1 -> {
                if (leaveTypeRsp1.getLiLeaveApprovals() != null) {
                    dataManager.setCacheLeaveType(leaveTypeRsp1);
                }
                return leaveTypeRsp1;
            });
        } else {
            return new Single<LeaveTypeRsp>() {
                @Override
                protected void subscribeActual(@NonNull SingleObserver<? super LeaveTypeRsp> observer) {
                    observer.onSuccess(dataManager.getCacheLeaveType());//post cache list
                }
            };
        }
    }
}
