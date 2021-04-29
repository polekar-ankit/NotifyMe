package com.gipl.swayam.ui.me;

import android.graphics.Bitmap;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.gipl.imagepicker.ImageResult;
import com.gipl.swayam.R;
import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.data.FirebaseDb;
import com.gipl.swayam.data.model.api.ApiError;
import com.gipl.swayam.data.model.api.dashbordcount.DashboardCountRsp;
import com.gipl.swayam.data.model.api.lib.utility.CheckOutType;
import com.gipl.swayam.data.model.api.sendotp.User;
import com.gipl.swayam.data.model.firebase.Employee;
import com.gipl.swayam.domain.UserUseCase;
import com.gipl.swayam.exceptions.CustomException;
import com.gipl.swayam.ui.base.BaseViewModel;
import com.gipl.swayam.ui.model.Response;
import com.gipl.swayam.uility.TimeUtility;
import com.gipl.swayam.uility.rx.SchedulerProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MeViewModel extends BaseViewModel {
    private final ObservableField<String> empName = new ObservableField<>("");
    private final ObservableField<String> empCode = new ObservableField<>("");
    private final ObservableField<String> empPlant = new ObservableField<>("");
    private final ObservableField<String> empMoNumber = new ObservableField<>("");
    private final UserUseCase userUseCase;
    private final ObservableField<String> checkInDateTime = new ObservableField<>("");
    private final ObservableField<String> checkInTime = new ObservableField<>("");
    private final ObservableField<String> misPunchTimeCount = new ObservableField<>("");
    private final ObservableBoolean showCheckInButton = new ObservableBoolean(true);
    private final FirebaseDb firebaseDb;
    private ObservableField<String> empImage = new ObservableField<>("https://preview.keenthemes.com/conquer/assets/img/profile/profile-img.png");
    private ExecutorService countDownTimer;
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.getValue() == null)
                return;
            Employee employee = new Employee(snapshot);
            if (getDataManager().getCheckType() != employee.getCheckType()) {
                getDataManager().setCheckType(employee.getCheckType());
                if (employee.getCheckType() == getDataManager().getUtility().getCheckType().getBitCheckIn()) {
                    getDataManager().setActiveShift(employee.getSuidShift());
                    getDataManager().setCheckInTime(employee.getCheckTime());
                    setCheckInTimer();
                } else {
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
    private ImageResult imageResult;

    public MeViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        userUseCase = new UserUseCase(dataManager);
        User user = dataManager.getUserObj();
        empName.set(user.getName());
        empCode.set(user.getEmpId());
        empMoNumber.set(user.getMobileNumber());
        empPlant.set(user.getPlant());
        if (user.getsProfilePicURL() != null && !user.getsProfilePicURL().isEmpty())
            empImage.set(user.getsProfilePicURL());
        firebaseDb = new FirebaseDb(empCode.get());
    }

    public ObservableField<String> getMisPunchTimeCount() {
        return misPunchTimeCount;
    }

    public void setEmpCheckInStatusListener() {
        firebaseDb.checkEmployeeData(valueEventListener);
    }

    public void removeEmpCheckInStatusListener() {
        firebaseDb.removeValueEvent(valueEventListener);
    }

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
                        CheckOutType checkOutType = getDataManager().getUtility().getCheckOutType();
//                        updateFirebaseNode(getDataManager().getActiveShift(), type, checkOutRsp.getTag());
                        getDataManager().setActiveShift("");
                        if (type == checkOutType.getBitDayEnd()) {
                            getResponseMutableLiveData().postValue(Response.success(R.string.day_end));
                            getDataManager().setCheckInTime(0);
                        } else if (type == checkOutType.getBitLunch())
                            getResponseMutableLiveData().postValue(Response.success(R.string.lunch_out));
                        else if (type == checkOutType.getBitOfficialOut())
                            getResponseMutableLiveData().postValue(Response.success(R.string.official_out));

                        showCheckInButton.set(checkInDateTime.get().isEmpty());

                    } else {
                        getResponseMutableLiveData().postValue(Response.error(new Exception(new CustomException(checkOutRsp.getApiError().getErrorMessage()))));
                    }
                }, throwable -> getResponseMutableLiveData().postValue(Response.error(throwable))));
    }

    public ObservableField<String> getEmpImage() {
        return empImage;
    }

    public void setEmpImage(ImageResult empImage) {
        this.imageResult = empImage;
        compressImage();
    }

    public void compressImage() {


        RequestOptions myOptions = new RequestOptions()
                .fitCenter() // or centerCropjClientType=2&sEmpCode=400031&suidSession=5239a4f100a06ef4a9cec6ea4762e063923cc91f2788718ca6dd9f4b8e5ebead&sTag=2021-04-28%2008%3A18&sProfileBase64=%2F9j%2F4AAQSkZJRgABAQAAAQABAAD%2F2wBDAAoHBwgHBgoICAgLCgoLDhgQDg0NDh0VFhEYIx8lJCIf%0AIiEmKzcvJik0KSEiMEExNDk7Pj4%2BJS5ESUM8SDc9Pjv%2F2wBDAQoLCw4NDhwQEBw7KCIoOzs7Ozs7%0AOzs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozv%2FwAARCABCAGQDASIA%0AAhEBAxEB%2F8QAGgAAAgMBAQAAAAAAAAAAAAAAAAUCAwYEAf%2FEADYQAAIBAwMCAwUGBgMBAAAAAAEC%0AAwAEEQUSITFBE1FhBhQiccEygZGhsdEHFTNCcvAjc4Px%2F8QAGQEBAQADAQAAAAAAAAAAAAAAAwIA%0AAQQF%2F8QAHREAAgIDAQEBAAAAAAAAAAAAAAECERIhMVEDQf%2FaAAwDAQACEQMRAD8A7oJrdJBtjZiR%0AyVHB%2Farp2e6gLW1ytoVPLsMkenkKzk%2Bp29o3u8SZulBVTJwTg46%2BtU3d9e3TQ2tsTACrFyhAx5%2FW%0AhpUI3seC892iSLb78q9XKgc%2BeScVxvr0U92EkxF1woHTy6DrSN7vwpYoLdSzHjJb8z%2BBqiVhI0ly%0Ax3CNdu7zPn%2BNSnZT0O5%2FaeygDJH40pXyGBSy79s7yRNkcAVR03sTSRLtI3ZnHiE8YxVM16shz4QW%0AkSCzR3za3qcwyZyueoUYpTdPJMC0shY%2BpzUzdhh1x8qolkDZ71RLkVRzSROjA7gn9rcjHlWr9mWs%0A5TcSxP4cyRkhSelZ610a9vV8SNUSPs0jhQf9zUr7QdT06JZpI98LLnxIW3qPnjp99Rab6Irq6IuI%0AJXLNeBHz8W9Sec%2BleCyikHGqWw9G3D6UvzRk1YdoYfyzPP8AM7M%2F%2BhH0opduNFYb0aGLWn1LVYXu%0AUjU42ZHGev713W8pit5%2BQrF9nGc%2FdSNbO227zcNkc%2FCtevdsG8NCxBOcnrnzo5O1SLimnbGSzfBJ%0AKXwxXjHUEnH6cVHUbnw4oLbgYTc2O%2Baogk3wPnBIX0PT0qGtA4trhVYK8e3J6ccYqYLRr67Kfhao%0ASIoGWOBVMcpFdMc7Lkw8yYyX2g7P8fX1pOAxjbOqOEWVuJLjRo542H25Hk3j1wrDH3io3EOlMqyx%0AJPF5qzZU%2FLgGuURzK3iR3cm48kknmou8pYePIXYY6npUt%2BHQo%2Bnc88rlXhfaBgBM9AOlWWepTWU6%0AvIkhj%2BywT9qrtIRcOqgfCTg57ffTR9Na1hMnxsg4%2BEHgUDdMdK0KNbsrd4hqFkAoJxOi9FY9CB2B%0ApJT5zDGzjxVEMwKOD0%2FPuOD6UkkChjtbcPOng21s5%2FqknaI0UUUgQyzhXwBnHANVHJKMRz2AFTbg%0ADHcYNSEYaI4%2B0OcUKZ0yOqyA3DMa%2BnnXfeWXvWlSQquJIf8AkjXuVHWlVtO8bgqxBz8POB61pNIu%0A4Li4y7OqxjAzyevb0q7oOrMSnFdcGQgKlSSeRWn1rStDSd3d5IZGwwC4II7nisvlBIyo25FbAZh2%0A88VqTvhkI09lg3lj3z2J5q2KxuJxviUsMYOa6NJVJbsLIMgjA%2Bf%2B%2FpWp0%2Bz8KTZGOGwSaLdi6ox0%0AYmtptrhkYHoRT3TvaTUbeEwxRxP2LSDjB455x%2F8Aan7dXsVpLa6fbpGZUTfLJgE5PQelZGW9uJY%2F%0ADZ8ITnYBgZ88UigG5mw0a7uriRtKf3a5snJEkZjAWPnOemev5isdqNo9hqFxaSgBoZCvGcEZ4Izz%0AgjkelQW4njdnjmdC67GKsRlfI%2BnAqomrjFpkSlkgoqOaKog7oEnu22JGM9izhVHOOScCroptqtG1%0AvIZAwB2jBU9MdDTfSNTMNu1xYy26XcMcMQjuJxGCuSZCN2BydvRgeprhtNbOmC%2BS2WJxPKMBlLZV%0ASSuHyDwwQjjnAzxkGcRcihoHnI2WdwpxuICE8YBz08iPxoWW40%2BZkaN43BGVYYYehFMZPau4vLYr%0AceGHwy8RHBHu5hzw3Ujb265PTgpdRu2vdSur0kbriZ5DtG0csT0ycdfM1lGWWzStMxLtk%2Bo6VAQt%0At8SMZXOM4rm3Z9a1cVrHBAicZjjyR158%2Fn1qcDeSFOmnwb5Gc7AuSd1ajTrue6lXbJFDGScbiQzY%0A%2B6kL2SOpIUHnHPH1qMEMtuw8OQ7TwVPOKRfOg3Ox%2Fd%2BxLarfSTR6pAzO5yZM4HpuHU8jsOtcY%2Fhx%0AqZLBpLQbRk7bjP0rvtlkhgVWLAsBnPY11xXs%2BwDd8I44ODUOSvQihrYof%2BGGqLCX95tg4zhDLyR%2B%0AFK5PYTXArOls0ir3jKt%2BQbNaG51mdbnaDhEPByflivE1%2BeM7tzkIAFweOuea6YwTVtnNKTTpIzo9%0Ag9cYArBkEA9QPrRT5J7q73TSy%2FET%2FcCT%2BRorVQ9N3LwwPc0eVFFGKS7V52oorDCy35uIgeQWX9a3%0AbAbLjgcIPrRRWLpp8EshO0cnlRUx%2FTT%2FADFFFN%2BAro9QkqSTk5NeL9o%2FL96KK849AUXP9RP%2BwfpV%0ALk88%2BVFFd64cD6NdLJ9yXk9TRRRXO%2BnSuH%2F%2F2Q%3D%3D%0A
                .override(200, 200);

        Glide.with(getDataManager().getContext())
                .asBitmap()
                .apply(myOptions)
                .load(imageResult.getsImagePath())
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e,
                                                Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model,
                                                   Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d("", "" + resource.toString());
                        getResponseMutableLiveData().postValue(Response.loading());
                        uploadImage(resource);
                        return false;
                    }
                }).submit();


    }

    private void uploadImage(Bitmap resource) {
        getCompositeDisposable().add(userUseCase.updateUserProfile(resource)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(rsp -> {
                    if (rsp.getApiError().getErrorVal() == ApiError.ERROR_CODE.OK) {
                        getResponseMutableLiveData().postValue(Response.success(null));
                        this.empImage.set(imageResult.getsImagePath());
                    } else {
                        getResponseMutableLiveData().postValue(Response.error(new Exception(new CustomException(rsp.getApiError().getErrorMessage()))));
                    }
                }, throwable -> getResponseMutableLiveData().postValue(Response.error(throwable))));
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

    public void logout() {
        getResponseMutableLiveData().postValue(Response.loading());
        getCompositeDisposable().add(userUseCase.Logout().subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(aBoolean -> getResponseMutableLiveData().postValue(Response.success("Logout")),
                        throwable -> getResponseMutableLiveData().postValue(Response.error(throwable))));
    }

    public void getDashboardCount() {
        try {
            DashboardCountRsp cout = getDataManager().getDashboardCount();
            if (cout != null)
                misPunchTimeCount.set(String.valueOf(cout.getCounts().getMissPunchCount()));
            getCompositeDisposable().add(userUseCase.getDashboardCount().subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .subscribe(dashboardCountRsp -> {
                        DashboardCountRsp rsp = getDataManager().getDashboardCount();
                        if (rsp != null)
                            misPunchTimeCount.set(String.valueOf(rsp.getCounts().getMissPunchCount()));
                    }, throwable -> {
                        DashboardCountRsp rsp = getDataManager().getDashboardCount();
                        if (rsp != null)
                            misPunchTimeCount.set(String.valueOf(rsp.getCounts().getMissPunchCount()));
                    }));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
