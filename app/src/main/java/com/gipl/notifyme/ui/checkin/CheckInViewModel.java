package com.gipl.notifyme.ui.checkin;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.gipl.notifyme.R;
import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.data.FirebaseDb;
import com.gipl.notifyme.data.model.api.ApiError;
import com.gipl.notifyme.data.model.api.lib.Shifts;
import com.gipl.notifyme.data.model.firebase.Employee;
import com.gipl.notifyme.domain.UserUseCase;
import com.gipl.notifyme.exceptions.CustomException;
import com.gipl.notifyme.ui.base.BaseViewModel;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.uility.TimeUtility;
import com.gipl.notifyme.uility.rx.SchedulerProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

public class CheckInViewModel extends BaseViewModel {
    FirebaseDb firebaseDb;
    private final MutableLiveData<ArrayList<Shifts>> shiftLiveData = new MutableLiveData<>();
    private final UserUseCase userUseCase;
    private String scanBarcode = "";
    private final ObservableField<String> shiftError = new ObservableField<>("");

    public CheckInViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        userUseCase = new UserUseCase(dataManager);
        firebaseDb = new FirebaseDb(dataManager.getEmpCode());
        getShiftData();

    }





    public void setScanBarcode(String scanBarcode) throws JSONException {
        this.scanBarcode = new JSONObject(scanBarcode).getString("uniqueId");
    }

    public MutableLiveData<ArrayList<Shifts>> getShiftLiveData() {
        return shiftLiveData;
    }

    private void getShiftData() {
        shiftLiveData.postValue((ArrayList<Shifts>) getDataManager().getShiftList());
    }

    public ObservableField<String> getShiftError() {
        return shiftError;
    }

    public void setShiftError(String shiftError) {
        this.shiftError.set(shiftError);
    }

    public void checkIn(String suidShift) {
        shiftError.set("");
        if (suidShift == null) {
            shiftError.set(getDataManager().getContext().getString(R.string.shift_not_select_error));
            return;
        }
        getResponseMutableLiveData().postValue(Response.loading());
        getCompositeDisposable().add(userUseCase.checkIn(suidShift, scanBarcode)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(checkInRsp -> {
                    if (checkInRsp.getApiError().getErrorVal() == ApiError.ERROR_CODE.OK) {
                        getResponseMutableLiveData().postValue(Response.success(true));
//                        updateFirebaseNode(suidShift,checkInRsp.getTag());
                    } else {
                        getResponseMutableLiveData().postValue(Response.error(new Exception(new CustomException(checkInRsp.getApiError().getErrorMessage()))));
                    }
                }, throwable -> getResponseMutableLiveData().postValue(Response.error(throwable))));
    }

    private void updateFirebaseNode(String suidShift,String checkTime) throws ParseException {
        Employee employee = new Employee(getDataManager().getUtility().getCheckType().getBitCheckIn(),
                suidShift, getDataManager().getCheckInTime(),
                getDataManager().getUtility().getCheckInType().getBitBySelf(),
                -1,
                TimeUtility.convertUtcTimeToLong(checkTime)
                );
        firebaseDb.setEmployeeCheckInStatus(employee, task -> {
            if (task.isSuccessful()) {
                getResponseMutableLiveData().postValue(Response.success(true));
            } else {
                getResponseMutableLiveData().postValue(Response.error(task.getException()));
            }

        });
    }
}
