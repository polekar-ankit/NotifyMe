package com.gipl.notifyme.domain;

import android.graphics.Color;

import com.gipl.notifyme.R;
import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.data.model.api.addovertime.AddOverTimeReq;
import com.gipl.notifyme.data.model.api.addovertime.AddOverTimeRsp;
import com.gipl.notifyme.data.model.api.lib.utility.StatusType;
import com.gipl.notifyme.data.model.api.mispunchlist.LiMissPunch;
import com.gipl.notifyme.data.model.api.mispunchlist.MissPunchListReq;
import com.gipl.notifyme.data.model.api.mispunchlist.MissPunchListRsp;
import com.gipl.notifyme.data.model.api.overtimelist.OT;
import com.gipl.notifyme.data.model.api.overtimelist.OverTimeListReq;
import com.gipl.notifyme.data.model.api.overtimelist.OverTimeListRsp;
import com.gipl.notifyme.data.model.api.punchingslip.AddPunchingSlipReq;
import com.gipl.notifyme.data.model.api.punchingslip.AddPunchingSlipRsp;
import com.gipl.notifyme.data.model.api.sendotp.User;
import com.gipl.notifyme.uility.TimeUtility;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public class SlipDomain extends UseCase {
    public SlipDomain(DataManager dataManager) {
        super(dataManager);
    }

    public Single<AddPunchingSlipRsp> addPunchingSlip(AddPunchingSlipReq req) {
        req.setSuidSession(dataManager.getSession());
        User user = dataManager.getUserObj();
        req.setSuidShift(user.getSuidUser());
        req.setSuidEmployee(user.getSuidUser());
        req.setEmpCode(dataManager.getEmpCode());
        req.setSuidUserAplicant(user.getSuidUser());
        req.setTag(TimeUtility.getCurrentUtcDateTimeForApi());
        req.setsExtraInfo("");
        return dataManager.addPunchingSlip(req);
    }

    public Single<MissPunchListRsp> getMissPunchList(MissPunchListReq req) {
        return dataManager.getMissPunchList(req).map(rsp -> {
            if (rsp.getLiMissPunch() == null) {
                return rsp;
            }
            StatusType statusType = dataManager.getUtility().getStatusType();
            for (LiMissPunch liMissPunch :
                    rsp.getLiMissPunch()) {
                if (liMissPunch.getStatus() == statusType.getBITVERIFIED()) {
                    liMissPunch.setColor(Color.parseColor("#43A047"));
                    liMissPunch.setStatusDis(dataManager.getContext().getString(R.string.lbl_status_verify));
                } else if (liMissPunch.getStatus() == statusType.getBITDELETED()) {
                    liMissPunch.setColor(Color.parseColor("#E53935"));
                    liMissPunch.setStatusDis(dataManager.getContext().getString(R.string.lbl_status_cancelled));
                } else if (liMissPunch.getStatus() == statusType.getBITACTIVE()) {
                    liMissPunch.setColor(Color.parseColor("#FB8C00"));
                    liMissPunch.setStatusDis(dataManager.getContext().getString(R.string.lbl_status_pending));
                }
            }
            return rsp;
        });
    }

    public Single<AddOverTimeRsp> addOverTime(AddOverTimeReq req) {
        return dataManager.addOvertime(req);
    }

    public Single<OverTimeListRsp> getOverTimeList(OverTimeListReq req) {
        return dataManager.getOvertimeList(req).map(rsp -> {
            if (rsp.getOT() == null)
                return rsp;

            StatusType statusType = dataManager.getUtility().getStatusType();
            for (OT ot :
                    rsp.getOT()) {

                double otHr = ot.getrOTHrs();
                double intensive = ot.getRIntesiveHrs();

                ot.setDisOT(String.valueOf(otHr + intensive));

                if (ot.getStatus() == statusType.getBITVERIFIED()) {
                    ot.setColor(Color.parseColor("#43A047"));
                    ot.setStatusDis(dataManager.getContext().getString(R.string.lbl_status_verify));
                } else if (ot.getStatus() == statusType.getBITDELETED()) {
                    ot.setColor(Color.parseColor("#E53935"));
                    ot.setStatusDis(dataManager.getContext().getString(R.string.lbl_status_cancelled));
                } else if (ot.getStatus() == statusType.getBITACTIVE()) {
                    ot.setColor(Color.parseColor("#FB8C00"));
                    ot.setStatusDis(dataManager.getContext().getString(R.string.lbl_status_pending));
                }
            }
            return rsp;
        });
    }
}