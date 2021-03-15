package com.gipl.notifyme.domain;

import android.graphics.Color;

import androidx.lifecycle.LiveData;

import com.gipl.notifyme.R;
import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.data.FirebaseDb;
import com.gipl.notifyme.data.model.api.addco.AddCoReq;
import com.gipl.notifyme.data.model.api.addco.AddCoRsp;
import com.gipl.notifyme.data.model.api.addovertime.AddOverTimeReq;
import com.gipl.notifyme.data.model.api.addovertime.AddOverTimeRsp;
import com.gipl.notifyme.data.model.api.colist.CO;
import com.gipl.notifyme.data.model.api.colist.CoListReq;
import com.gipl.notifyme.data.model.api.colist.CoListRsp;
import com.gipl.notifyme.data.model.api.lib.utility.LeaveFor;
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
import com.gipl.notifyme.data.model.api.shiftchange.ShiftChangeReq;
import com.gipl.notifyme.data.model.api.shiftchange.ShiftChangeRsp;
import com.gipl.notifyme.data.model.api.shiftchangelist.Scr;
import com.gipl.notifyme.data.model.api.shiftchangelist.ShiftChangeListReq;
import com.gipl.notifyme.data.model.api.shiftchangelist.ShiftChangeListRsp;
import com.gipl.notifyme.ui.addco.AddCoFragment;
import com.gipl.notifyme.ui.addco.AddCoFragment.CO_FOR;
import com.gipl.notifyme.ui.model.Reason;
import com.gipl.notifyme.uility.TimeUtility;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import io.reactivex.Single;

/**
 * employee's slip such as over time,shift change ,add co,punching slip etc related api are handle form here
 */
public class SlipDomain extends UseCase {
    public SlipDomain(DataManager dataManager) {
        super(dataManager);
    }

    public Single<AddPunchingSlipRsp> addPunchingSlip(AddPunchingSlipReq req) {
        req.setSuidSession(dataManager.getSession());
        User user = dataManager.getUserObj();
//        req.setSuidShift(user.getSuidUser());
        req.setSuidUser(user.getSuidUser());
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
            Collections.sort(rsp.getLiMissPunch(), (o1, o2) -> {
                SimpleDateFormat sim = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
                try {
                    return sim.parse(o2.getDtMissPunch()).compareTo(sim.parse(o1.getDtMissPunch()));
                } catch (Exception e) {
                    return 0;
                }
            });
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
            Collections.sort(rsp.getOT(), (o1, o2) -> {
                SimpleDateFormat sim = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
                try {
                    return sim.parse(o2.getSDtOverTime()).compareTo(sim.parse(o1.getSDtOverTime()));
                } catch (Exception e) {
                    return 0;
                }
            });
            return rsp;
        });
    }

    public Single<ShiftChangeRsp> addShiftChange(ShiftChangeReq req) {
        return dataManager.shiftChangeRequest(req);
    }

    public Single<ShiftChangeListRsp> getShiftChangeList(ShiftChangeListReq req) {
        return dataManager.getShiftChangeList(req).map(rsp -> {
            if (rsp.getScr() == null)
                return rsp;

            StatusType statusType = dataManager.getUtility().getStatusType();
            for (Scr scr :
                    rsp.getScr()) {
                if (scr.getStatus() == statusType.getBITVERIFIED()) {
                    scr.setColor(Color.parseColor("#43A047"));
                    scr.setStatusDis(dataManager.getContext().getString(R.string.lbl_status_verify));
                } else if (scr.getStatus() == statusType.getBITDELETED()) {
                    scr.setColor(Color.parseColor("#E53935"));
                    scr.setStatusDis(dataManager.getContext().getString(R.string.lbl_status_cancelled));
                } else if (scr.getStatus() == statusType.getBITACTIVE()) {
                    scr.setColor(Color.parseColor("#FB8C00"));
                    scr.setStatusDis(dataManager.getContext().getString(R.string.lbl_status_pending));
                }
            }
            Collections.sort(rsp.getScr(), (o1, o2) -> {
                SimpleDateFormat sim = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
                try {
                    return sim.parse(o2.getDtShiftFrom()).compareTo(sim.parse(o1.getDtShiftFrom()));
                } catch (Exception e) {
                    return 0;
                }
            });
            return rsp;
        });
    }

    public Single<AddCoRsp> addCo(AddCoReq req) {
        return dataManager.addCo(req);
    }

    public Single<CoListRsp> getCoList(CoListReq req) {
        return dataManager.getCoList(req).map(rsp -> {
            if (rsp.getCO() == null)
                return rsp;
            StatusType statusType = dataManager.getUtility().getStatusType();
            LeaveFor leaveFor = dataManager.getUtility().getLeaveFor();
            for (CO co :
                    rsp.getCO()) {
                if (co.getStatus() == statusType.getBITVERIFIED()) {
                    co.setColor(Color.parseColor("#43A047"));
                    co.setStatusDis(dataManager.getContext().getString(R.string.lbl_status_verify));
                } else if (co.getStatus() == statusType.getBITDELETED()) {
                    co.setColor(Color.parseColor("#E53935"));
                    co.setStatusDis(dataManager.getContext().getString(R.string.lbl_status_cancelled));
                } else if (co.getStatus() == statusType.getBITACTIVE()) {
                    co.setColor(Color.parseColor("#FB8C00"));
                    co.setStatusDis(dataManager.getContext().getString(R.string.lbl_status_pending));
                }
                if (co.getCOFor() == CO_FOR.HALF_DAY.getValue()) {
                    co.setLblCOFor( dataManager.getContext().getString(R.string.lbl_co_half_day));
                } else if (co.getCOFor() == CO_FOR.FULL_DAY.getValue()) {
                    co.setLblCOFor(dataManager.getContext().getString(R.string.lbl_full_day));
                } else if (co.getCOFor() == CO_FOR.ONE_AND_HALF_DAYS.getValue()) {
                    co.setLblCOFor(dataManager.getContext().getString(R.string.lbl_co_one_half_day));
                } else if (co.getCOFor() == CO_FOR.TWO_DAYS.getValue()) {
                    co.setLblCOFor(dataManager.getContext().getString(R.string.lbl_co_two));
                }
            }
            Collections.sort(rsp.getCO(), (o1, o2) -> {
                SimpleDateFormat sim = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
                try {
                    return sim.parse(o2.getDtCO()).compareTo(sim.parse(o1.getDtCO()));
                } catch (Exception e) {
                    return 0;
                }
            });
            return rsp;
        });
    }

    public LiveData<List<Reason>> getReasonLocal(String type) {
        return dataManager.getReasonList(type);
    }

    public void checkAndRefreshReason(String type) {
        long lastSync = dataManager.getReasonCacheDate(type);
        if (lastSync == 0) {
            new FirebaseDb().getReason(type, dataManager);
            return;
        }
//        long days = TimeUtility.getDiff(lastSync) / (1000 * 60 * 60 * 24);
        long days = TimeUtility.getDifferenceInDaysUpdateToCurrentTime(lastSync);
        ;
        if (days >= 1) {
            new FirebaseDb().getReason(type, dataManager);
        }
    }
}
