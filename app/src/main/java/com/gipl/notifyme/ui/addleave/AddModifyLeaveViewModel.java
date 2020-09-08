package com.gipl.notifyme.ui.addleave;

import android.widget.TextView;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.data.model.api.ApiError;
import com.gipl.notifyme.data.model.api.applyleave.AddModifyLeaveReq;
import com.gipl.notifyme.data.model.api.applyleave.AddModifyLeaveRsp;
import com.gipl.notifyme.data.model.api.leavetype.LeaveApproval;
import com.gipl.notifyme.data.model.api.leavetype.LeaveTypeRsp;
import com.gipl.notifyme.domain.LeaveDomain;
import com.gipl.notifyme.exceptions.CustomException;
import com.gipl.notifyme.ui.base.BaseViewModel;
import com.gipl.notifyme.ui.model.LeaveFor;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.uility.TimeUtility;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

import java.text.ParseException;
import java.util.ArrayList;

import io.reactivex.functions.Consumer;

public class AddModifyLeaveViewModel extends BaseViewModel {
    private ObservableField<String> reason = new ObservableField<>();
    private LeaveDomain leaveDomain;
    private MutableLiveData<ArrayList<LeaveApproval>> leaveTypeLiveData = new MutableLiveData<>();

    public AddModifyLeaveViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        leaveDomain = new LeaveDomain(dataManager);
    }

    public MutableLiveData<ArrayList<LeaveApproval>> getLeaveTypeLiveData() {
        return leaveTypeLiveData;
    }

    public void getLeaveList() {
        getResponseMutableLiveData().postValue(Response.loading());
        getCompositeDisposable().add(leaveDomain.getLeaveType()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(leaveTypeRsp -> {
                    if (leaveTypeRsp.getApiError().getErrorVal() == ApiError.ERROR_CODE.OK) {
                        getResponseMutableLiveData().postValue(Response.success(""));
                        getLeaveTypeLiveData().postValue(leaveTypeRsp.getLiLeaveApprovals());
                    } else {
                        getResponseMutableLiveData().postValue(Response.error(new Exception(new CustomException(leaveTypeRsp.getApiError().getErrorMessage()))));
                    }
                }, throwable -> getResponseMutableLiveData().postValue(Response.error(throwable))));
    }

    public void addModifyLeave(String from, String to, LeaveFor leaveFor, LeaveApproval suidLeaveType) {
        if (leaveFor.getSuid() == -1 || suidLeaveType.getSApprovalsRequired() == null) {
            return;
        }
        AddModifyLeaveReq addModifyLeaveReq = new AddModifyLeaveReq();
        try {
            addModifyLeaveReq.setsFrom(TimeUtility.convertDisplayDateToApi(from));
        } catch (ParseException e) {
            addModifyLeaveReq.setsFrom(from);
        }
        try {
            addModifyLeaveReq.setsTo(TimeUtility.convertDisplayDateToApi(to));
        } catch (ParseException e) {
            addModifyLeaveReq.setsTo(to);
        }
        addModifyLeaveReq.setjLeaveStatus(getDataManager().getUtility().getLeaveStatus().getBitPending());
        addModifyLeaveReq.setjLeaveFor(leaveFor.getSuid());
        addModifyLeaveReq.setSuidLeaveType(suidLeaveType.getSuidLeaveApproval());
        addModifyLeaveReq.setsReason(reason.get());

        getResponseMutableLiveData().postValue(Response.loading());

        getCompositeDisposable().add(leaveDomain.addModifyLeave(addModifyLeaveReq)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(addModifyLeaveRsp -> {
                    if (addModifyLeaveRsp.getApiError().getErrorVal() == ApiError.ERROR_CODE.OK) {
                        getResponseMutableLiveData().postValue(Response.success(AddModifyLeaveReq.class.getSimpleName()));
                    } else {
                        getResponseMutableLiveData().postValue(Response.error(new Exception(new CustomException(addModifyLeaveRsp.getApiError().getErrorMessage()))));
                    }
                }, throwable -> getResponseMutableLiveData().postValue(Response.error(throwable))));
    }

    public ObservableField<String> getReason() {
        return reason;
    }
}
