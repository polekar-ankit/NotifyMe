package com.gipl.swayam.ui.addleave;

import android.util.Base64;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gipl.swayam.R;
import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.data.model.api.ApiError;
import com.gipl.swayam.data.model.api.applyleave.AddModifyLeaveReq;
import com.gipl.swayam.data.model.api.leavetype.LeaveApproval;
import com.gipl.swayam.domain.LeaveDomain;
import com.gipl.swayam.domain.SlipDomain;
import com.gipl.swayam.exceptions.CustomException;
import com.gipl.swayam.ui.base.BaseViewModel;
import com.gipl.swayam.ui.model.LeaveFor;
import com.gipl.swayam.ui.model.Reason;
import com.gipl.swayam.ui.model.Response;
import com.gipl.swayam.uility.TimeUtility;
import com.gipl.swayam.uility.rx.SchedulerProvider;
import com.jaiselrahman.filepicker.model.MediaFile;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AddModifyLeaveViewModel extends BaseViewModel {
    private final ObservableField<String> reason = new ObservableField<>("");
    private final ObservableField<String> reasonError = new ObservableField<>("");
    private final ObservableField<String> sessionToError = new ObservableField<>("");
    private final ObservableField<String> sessionFromError = new ObservableField<>("");
    private final ObservableField<String> leaveTypeError = new ObservableField<>("");
    private final LeaveDomain leaveDomain;
    private final MutableLiveData<ArrayList<LeaveApproval>> leaveTypeLiveData = new MutableLiveData<>();
    AddModifyLeaveReq addModifyLeaveReq = new AddModifyLeaveReq();
    private MediaFile attachment;
    private MutableLiveData<Double> isLeaveDataValid = new MutableLiveData<>();

    public AddModifyLeaveViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        leaveDomain = new LeaveDomain(dataManager);
    }

    public ObservableField<String> getLeaveTypeError() {
        return leaveTypeError;
    }

    public ObservableField<String> getSessionToError() {
        return sessionToError;
    }

    public ObservableField<String> getSessionFromError() {
        return sessionFromError;
    }

    public ObservableField<String> getReasonError() {
        return reasonError;
    }

    public LiveData<List<Reason>> getPreDefineReasonList() {
        return new SlipDomain(getDataManager()).getReasonLocal(Reason.Type.LEAVE_REASON);
    }

    public void setAttachment(MediaFile attachment) {
        this.attachment = attachment;
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

    public MutableLiveData<Double> getIsLeaveDataValid() {
        return isLeaveDataValid;
    }

    public void checkLeaveDataForError(String from, String to,
                                       LeaveFor leaveFor,
                                       LeaveFor leaveForTo,
                                       LeaveApproval suidLeaveType,
                                       Reason selectedReason,
                                       long numberOfLeaveDays
    ) {
        leaveTypeError.set("");
        sessionToError.set("");
        sessionFromError.set("");
        reasonError.set("");

        if (!isNetworkConnected()) {
            getResponseMutableLiveData().postValue(Response.error(new Exception(new ConnectException())));
            return;
        }

        if (leaveFor.getSuid() == -1) {
            sessionFromError.set(getDataManager().getContext().getString(R.string.error_session_not_selected));
        }
        if (suidLeaveType == null || suidLeaveType.getSuidLeaveApproval/*getSApprovalsRequired*/() == null) {
            leaveTypeError.set(getDataManager().getContext().getString(R.string.error_select_leave));
        }
        if (numberOfLeaveDays > 1) {
            if (leaveForTo.getSuid() == -1) {
                sessionToError.set(getDataManager().getContext().getString(R.string.error_session_not_selected));
            }
        }

        if (selectedReason.getSuid() == -1) {
            reasonError.set(getDataManager().getContext().getString(R.string.error_leave_reason_not_selected));
        }

        if (selectedReason.getSuid() == 32 && reason.get().isEmpty()) {
            reasonError.set(getDataManager().getContext().getString(R.string.error_leave_reason_empty));
        }
        if (!leaveTypeError.get().isEmpty() || !sessionToError.get().isEmpty() || !sessionFromError.get().isEmpty() || !reasonError.get().isEmpty()) {
            return;
        }

        if (suidLeaveType.getSLeaveType().equals("SL")) {
            try {
                Calendar calFrom = TimeUtility.convertDisplayDateTimeToCalender(from);
                Calendar calTo = TimeUtility.convertDisplayDateTimeToCalender(to);
                double diff = calTo.getTimeInMillis() - calFrom.getTimeInMillis();
                double threeDaysInMili = TimeUnit.DAYS.toMillis(3);
                if (diff >= threeDaysInMili && attachment == null) {//if SL is above 3 days then medical certificate is required
                    getResponseMutableLiveData().postValue(Response.error(new Exception(new CustomException(getDataManager().getContext().getString(R.string.sl_certificate_not_available)))));
                    return;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

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

        addModifyLeaveReq.setjLeaveTo(leaveForTo.getSuid());
//        addModifyLeaveReq.setjLeaveStatus(getDataManager().getUtility().getLeaveStatus().getBitPending());
        addModifyLeaveReq.setjLeaveFor(leaveFor.getSuid());
        addModifyLeaveReq.setSuidLeaveType(suidLeaveType.getSuidLeaveApproval());
        addModifyLeaveReq.setsReason(selectedReason.getSuid() == 32 ? this.reason.get() : selectedReason.getReason());

        if (attachment != null)
            addModifyLeaveReq.setArrFilesBase64(new String[]{getBase64OfAttachFile()});
        com.gipl.swayam.data.model.api.lib.utility.LeaveFor utility = getDataManager().getUtility().getLeaveFor();
        if (numberOfLeaveDays > 1) {
            /*
              if leave start in first session of from date then from date is consider as full day
              and if it start in second session of from date then form date is consider as half day

              if leave start in first session of To date then To date is consider as full day
              and if it start in second session of To date then To date is consider as half day

              so we are making -0.5 from number of leave days for second session of from date and first session of to date
             */
            double finalNoOfLeaveDays = numberOfLeaveDays;
            if (leaveFor.getSuid() == utility.getBitSecondHalfDay())
                finalNoOfLeaveDays = finalNoOfLeaveDays - 0.5;

            if (leaveForTo.getSuid() == utility.getBitFirstHalfDay())
                finalNoOfLeaveDays = finalNoOfLeaveDays - 0.5;

            addModifyLeaveReq.setDays(finalNoOfLeaveDays);
            getIsLeaveDataValid().postValue(finalNoOfLeaveDays);
        } else {
            double finalNoOfLeaveDays = 1;
            if (leaveFor.getSuid() == utility.getBitSecondHalfDay()
                    || leaveFor.getSuid() == utility.getBitFirstHalfDay())
                finalNoOfLeaveDays = 0.5;
            addModifyLeaveReq.setDays(finalNoOfLeaveDays);
            getIsLeaveDataValid().postValue(0.0);
        }
    }

    public void addModifyLeave() {
        getResponseMutableLiveData().postValue(Response.loading());
        try {
            getCompositeDisposable().add(leaveDomain.addModifyLeave(addModifyLeaveReq)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .subscribe(addModifyLeaveRsp -> {
                        if (addModifyLeaveRsp.getApiError().getErrorVal() == ApiError.ERROR_CODE.OK) {
                            attachment = null;
                            getResponseMutableLiveData().postValue(Response.success(AddModifyLeaveReq.class.getSimpleName()));
                        } else {
                            getResponseMutableLiveData().postValue(Response.error(new Exception(new CustomException(addModifyLeaveRsp.getApiError().getErrorMessage()))));
                        }
                    }, throwable -> getResponseMutableLiveData().postValue(Response.error(throwable))));
        } catch (JSONException e) {
            e.printStackTrace();
            getResponseMutableLiveData().postValue(Response.error(e));
        }
    }

    private String getBase64OfAttachFile() {
        try {
            InputStream in = getDataManager().getContext().getContentResolver().openInputStream(attachment.getUri());
            byte[] bytes = getBytes(in);
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public ObservableField<String> getReason() {
        return reason;
    }

    public String getFinalReason() {
        return addModifyLeaveReq.getsReason();
    }
}
