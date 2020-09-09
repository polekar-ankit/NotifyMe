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

import io.reactivex.Single;

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

    public Single<AddModifyLeaveRsp> addModifyLeave(AddModifyLeaveReq addModifyLeaveReq) {
        addModifyLeaveReq.setSuidSession(dataManager.getSession());
//        if (BuildConfig.DEBUG)
//            addModifyLeaveReq.setSuidUser("bed4167a404add6345172c0a47f923fdb9cc15aedbcb04fca4a9c5cd3c42e7b9");
//        else
            addModifyLeaveReq.setSuidUser(dataManager.getUserObj().getSuidUser());
        addModifyLeaveReq.setsEmpCode(dataManager.getEmpCode());
        return dataManager.addModifyLeave(addModifyLeaveReq);
    }

    public Single<LeaveTypeRsp> getLeaveType() {
        LeaveTypeReq leaveTypeReq = new LeaveTypeReq();
        leaveTypeReq.setSuidSession(dataManager.getSession());
        leaveTypeReq.setPagination(false);
        leaveTypeReq.setTag(TimeUtility.getCurrentUtcDateTimeForApi());
        return dataManager.getLeaveType(leaveTypeReq);
    }
}
