package com.gipl.notifyme.ui.addleave;

import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gipl.notifyme.R;
import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.data.model.api.ApiError;
import com.gipl.notifyme.data.model.api.applyleave.AddModifyLeaveReq;
import com.gipl.notifyme.data.model.api.applyleave.AddModifyLeaveRsp;
import com.gipl.notifyme.data.model.api.leavetype.LeaveApproval;
import com.gipl.notifyme.data.model.api.leavetype.LeaveTypeRsp;
import com.gipl.notifyme.domain.LeaveDomain;
import com.gipl.notifyme.domain.SlipDomain;
import com.gipl.notifyme.exceptions.CustomException;
import com.gipl.notifyme.ui.base.BaseViewModel;
import com.gipl.notifyme.ui.model.LeaveFor;
import com.gipl.notifyme.ui.model.Reason;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.uility.TimeUtility;
import com.gipl.notifyme.uility.rx.SchedulerProvider;
import com.jaiselrahman.filepicker.model.MediaFile;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

public class AddModifyLeaveViewModel extends BaseViewModel {
    private ObservableField<String> reason = new ObservableField<>("");
    private ObservableField<String> reasonError = new ObservableField<>("");
    private LeaveDomain leaveDomain;
    private MutableLiveData<ArrayList<LeaveApproval>> leaveTypeLiveData = new MutableLiveData<>();
    private MediaFile attachment;

    public AddModifyLeaveViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        leaveDomain = new LeaveDomain(dataManager);
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

    public void addModifyLeave(String from, String to,
                               LeaveFor leaveFor,
                               LeaveApproval suidLeaveType,
                               Reason selectedReason) {
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
        if (selectedReason.getSuid() == -1) {
            reasonError.set(getDataManager().getContext().getString(R.string.error_leave_reason_not_selected));
        }

        if (selectedReason.getSuid() == 32 && reason.get().isEmpty()) {
            reasonError.set(getDataManager().getContext().getString(R.string.error_leave_reason_empty));
        }
        if (!reasonError.get().isEmpty()) {
            return;
        }

        addModifyLeaveReq.setjLeaveStatus(getDataManager().getUtility().getLeaveStatus().getBitPending());
        addModifyLeaveReq.setjLeaveFor(leaveFor.getSuid());
        addModifyLeaveReq.setSuidLeaveType(suidLeaveType.getSuidLeaveApproval());
        addModifyLeaveReq.setsReason(selectedReason.getSuid() == 32 ? this.reason.get() : selectedReason.getReason());

        if (attachment != null)
            addModifyLeaveReq.setArrFilesBase64(new String[]{getBase64OfAttachFile()});
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
            String ansValue = Base64.encodeToString(bytes, Base64.DEFAULT);
            return ansValue;
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
}
