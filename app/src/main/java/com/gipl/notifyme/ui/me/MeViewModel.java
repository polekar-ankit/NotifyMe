package com.gipl.notifyme.ui.me;

import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.gipl.notifyme.R;
import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.data.FirebaseDb;
import com.gipl.notifyme.data.model.api.ApiError;
import com.gipl.notifyme.data.model.api.lib.utility.CheckOutType;
import com.gipl.notifyme.data.model.api.sendotp.User;
import com.gipl.notifyme.data.model.firebase.Employee;
import com.gipl.notifyme.domain.UserUseCase;
import com.gipl.notifyme.exceptions.CustomException;
import com.gipl.notifyme.ui.base.BaseViewModel;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.uility.TimeUtility;
import com.gipl.notifyme.uility.rx.SchedulerProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MeViewModel extends BaseViewModel {
    private ObservableField<String> empName = new ObservableField<>("");
    private ObservableField<String> empCode = new ObservableField<>("");
    private ObservableField<String> empPlant = new ObservableField<>("");
    private ObservableField<String> empMoNumber = new ObservableField<>("");
    private ObservableField<String> empImage = new ObservableField<>("https://preview.keenthemes.com/conquer/assets/img/profile/profile-img.png");
    private UserUseCase userUseCase;
    private ExecutorService countDownTimer;
    private ObservableField<String> checkInDateTime = new ObservableField<>("");
    private ObservableField<String> checkInTime = new ObservableField<>("");
    private ObservableBoolean showCheckInButton = new ObservableBoolean(true);
    private FirebaseDb firebaseDb;

    public MeViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        userUseCase = new UserUseCase(dataManager);
        User user = dataManager.getUserObj();
        empName.set(user.getName());
        empCode.set(user.getEmpId());
        empMoNumber.set(user.getMobileNumber());
        empPlant.set(user.getPlant());
        firebaseDb = new FirebaseDb(empCode.get());

    }

    public void setEmpCheckInStatusListener() {
        firebaseDb.checkEmployeeData(valueEventListener);
    }

    public void removeEmpCheckInStatusListener() {
        firebaseDb.removeValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Log.d("firebase", snapshot.toString());
                Employee employee = new Employee(snapshot);
                if (getDataManager().getCheckType()!=employee.getCheckType()){
                    getDataManager().setCheckType(employee.getCheckType());
                    if (employee.getCheckType()==getDataManager().getUtility().getCheckType().getBitCheckIn()){
                        getDataManager().setActiveShift(employee.getSuidShift());
                        getDataManager().setCheckInTime(employee.getCheckTime());
                        setCheckInTimer();
                    }
                    else {
                        endTimer();
                        checkInDateTime.set("");
                        checkInTime.set("");
                        showCheckInButton.set(checkInDateTime.get().isEmpty());
                        getDataManager().setActiveShift("");
                        endTimer();
                        if (employee.getCheckOutType() == getDataManager().getUtility().getCheckOutType().getBitDayEnd()) {
                            getDataManager().setCheckInTime(0);
                        }
                    }
                }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    public ObservableBoolean getShowCheckInButton() {
        return showCheckInButton;
    }

    public ObservableField<String> getCheckInDateTime() {
        return checkInDateTime;
    }

    public ObservableField<String> getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTimer() {
        if (!getDataManager().getActiveShift().isEmpty()) {
            long checkInMili = getDataManager().getCheckInTime();
            checkInDateTime.set(TimeUtility.convertUtcMilisecondToDisplay(checkInMili));
            countDownTimer = Executors.newSingleThreadExecutor();
            countDownTimer.execute(() -> {
                while (!countDownTimer.isShutdown()) {
                    checkInTime.set(TimeUtility.getCountDownTimer(TimeUtility.getDiff(checkInMili)));
                    SystemClock.sleep(1000);
                }
            });

        } else {
            checkInDateTime.set("");
        }
        showCheckInButton.set(checkInDateTime.get().isEmpty());
    }

    public void endTimer() {
        if (countDownTimer != null)
            countDownTimer.shutdown();
    }

    public void checkOut(int type) {
        getResponseMutableLiveData().postValue(Response.loading());
        getCompositeDisposable().add(userUseCase.checkOut(type)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(checkOutRsp -> {
                    if (checkOutRsp.getApiError().getErrorVal() == ApiError.ERROR_CODE.OK) {
                        endTimer();
                        checkInDateTime.set("");
                        checkInTime.set("");
                        updateFirebaseNode(getDataManager().getActiveShift(), type, checkOutRsp.getTag());
                        getDataManager().setActiveShift("");
                    } else {
                        getResponseMutableLiveData().postValue(Response.error(new Exception(new CustomException(checkOutRsp.getApiError().getErrorMessage()))));
                    }
                }, throwable -> getResponseMutableLiveData().postValue(Response.error(throwable))));
    }

    private void updateFirebaseNode(String suidShift, int type, String checkTime) throws ParseException {
        CheckOutType checkOutType = getDataManager().getUtility().getCheckOutType();
        Employee employee = new Employee(getDataManager().getUtility().getCheckType().getBitCheckOut(),
                suidShift,
                type==checkOutType.getBitDayEnd()?TimeUtility.convertUtcTimeToLong(checkTime):getDataManager().getCheckInTime(),
                getDataManager().getUtility().getCheckInType().getBitBySelf(), type);

        getDataManager().setCheckType(getDataManager().getUtility().getCheckType().getBitCheckOut());

        firebaseDb.setEmployeeCheckInStatus(employee, task -> {
            if (task.isSuccessful()) {
                if (type == checkOutType.getBitDayEnd()) {
                    getResponseMutableLiveData().postValue(Response.success(R.string.day_end));
                    getDataManager().setCheckInTime(0);
                } else if (type == checkOutType.getBitLunch())
                    getResponseMutableLiveData().postValue(Response.success(R.string.lunch_out));
                else if (type == checkOutType.getBitOfficialOut())
                    getResponseMutableLiveData().postValue(Response.success(R.string.official_out));

                showCheckInButton.set(checkInDateTime.get().isEmpty());
            } else {
                getResponseMutableLiveData().postValue(Response.error(task.getException()));
            }

        });
    }

    public ObservableField<String> getEmpImage() {
        return empImage;
    }

    public ObservableField<String> getEmpName() {
        return empName;
    }

    public ObservableField<String> getEmpCode() {
        return empCode;
    }

    public ObservableField<String> getEmpPlant() {
        return empPlant;
    }

    public ObservableField<String> getEmpMoNumber() {
        return empMoNumber;
    }
}
