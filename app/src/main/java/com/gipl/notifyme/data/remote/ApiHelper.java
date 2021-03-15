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

package com.gipl.notifyme.data.remote;

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
import com.gipl.notifyme.data.model.api.leavebalance.LeaveBalanceReq;
import com.gipl.notifyme.data.model.api.leavebalance.LeaveBalanceRsp;
import com.gipl.notifyme.data.model.api.leaves.GetLeaveRsp;
import com.gipl.notifyme.data.model.api.leaves.GetLeavesReq;
import com.gipl.notifyme.data.model.api.leavetype.LeaveTypeReq;
import com.gipl.notifyme.data.model.api.leavetype.LeaveTypeRsp;
import com.gipl.notifyme.data.model.api.lib.GetLibReq;
import com.gipl.notifyme.data.model.api.lib.GetLibRes;
import com.gipl.notifyme.data.model.api.mispunchlist.MissPunchListReq;
import com.gipl.notifyme.data.model.api.mispunchlist.MissPunchListRsp;
import com.gipl.notifyme.data.model.api.notification.GetNotificationRes;
import com.gipl.notifyme.data.model.api.notification.GetNotificationsReq;
import com.gipl.notifyme.data.model.api.overtimelist.OverTimeListReq;
import com.gipl.notifyme.data.model.api.overtimelist.OverTimeListRsp;
import com.gipl.notifyme.data.model.api.punchingslip.AddPunchingSlipReq;
import com.gipl.notifyme.data.model.api.punchingslip.AddPunchingSlipRsp;
import com.gipl.notifyme.data.model.api.sendotp.SendOTPReq;
import com.gipl.notifyme.data.model.api.sendotp.SendOtpRes;
import com.gipl.notifyme.data.model.api.shiftchange.ShiftChangeReq;
import com.gipl.notifyme.data.model.api.shiftchange.ShiftChangeRsp;
import com.gipl.notifyme.data.model.api.shiftchangelist.ShiftChangeListReq;
import com.gipl.notifyme.data.model.api.shiftchangelist.ShiftChangeListRsp;
import com.gipl.notifyme.data.model.api.usershift.UserShiftReq;
import com.gipl.notifyme.data.model.api.usershift.UserShiftRsp;
import com.gipl.notifyme.data.model.api.verifyotp.VerifyOtpReq;
import com.gipl.notifyme.data.model.api.verifyotp.VerifyOtpRsp;

import org.json.JSONException;

import io.reactivex.Single;

/**
 * Created by amitshekhar on 07/07/17.
 */

public interface ApiHelper {
    Single<SendOtpRes> sendOtp(SendOTPReq sendOTPReq);

    Single<VerifyOtpRsp> verifyOtp(VerifyOtpReq verifyOtpReq);

    Single<GetLibRes> getLib(GetLibReq getLibReq);

    Single<GetNotificationRes> getNotifications(GetNotificationsReq req);

    Single<CheckInRsp> checkIn(CheckInReq checkInReq);

    Single<CheckOutRsp> checkOut(CheckOutReq checkOutReq);

    //leave
    Single<GetLeaveRsp> getLeaveRequestList(GetLeavesReq getLeavesListReq);

    Single<AddModifyLeaveRsp> addModifyLeave(AddModifyLeaveReq addModifyLeaveReq) throws JSONException;

    Single<LeaveTypeRsp> getLeaveType(LeaveTypeReq leaveTypeReq);

    Single<LeaveBalanceRsp> getLeaveBalance(LeaveBalanceReq req);

    //punching slip
    Single<AddPunchingSlipRsp> addPunchingSlip(AddPunchingSlipReq req);

    Single<MissPunchListRsp> getMissPunchList(MissPunchListReq req);

    //Over time
    Single<AddOverTimeRsp> addOvertime(AddOverTimeReq addOverTimeReq);

    Single<OverTimeListRsp> getOvertimeList(OverTimeListReq req);

    //Shift change request
    Single<ShiftChangeRsp> shiftChangeRequest(ShiftChangeReq req);

    Single<ShiftChangeListRsp> getShiftChangeList(ShiftChangeListReq req);

    Single<UserShiftRsp> getUserShift(UserShiftReq userShiftReq);

    //CO
    Single<AddCoRsp> addCo(AddCoReq req);

    Single<CoListRsp> getCoList(CoListReq req);
}
