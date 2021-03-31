package com.gipl.swayam.ui.notification;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;

import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.data.model.api.ApiError;
import com.gipl.swayam.data.model.api.notification.GetNotificationsReq;
import com.gipl.swayam.data.model.api.notification.Notification;
import com.gipl.swayam.domain.NotificationUseCase;
import com.gipl.swayam.exceptions.CustomException;
import com.gipl.swayam.ui.base.BaseViewModel;
import com.gipl.swayam.ui.model.Response;
import com.gipl.swayam.uility.rx.SchedulerProvider;

import java.util.ArrayList;

public class NotificationListViewModel extends BaseViewModel {
    private final NotificationUseCase useCase;
    private final ObservableField<String> lastSync = new ObservableField<>("");

    public NotificationListViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        useCase = new NotificationUseCase(dataManager);
        lastSync.set(dataManager.getLastSync());

    }

    public LiveData<ArrayList<Notification>> getNotificationList() {
        return useCase.getNotificationList();
    }

    public ObservableField<String> getLastSync() {
        return lastSync;
    }

    public void getAllNotifications() {
        if (isNetworkConnected()) {
            GetNotificationsReq req = new GetNotificationsReq();
//            req.setSuidSession(getDataManager().getSession());
//            req.setTag(TimeUtility.getCurrentUtcDateTimeForApi());
            req.setStart(getDataManager().getCacheNotificationCount());
            if (req.getStart() == 0) {
                getResponseMutableLiveData().setValue(Response.loading());
                req.setPagination(false);
                req.setCount(10);
            } else {
                req.setPagination(true);
                req.setCount(120);
            }

            req.setEmpCode(getDataManager().getEmpCode());
            getCompositeDisposable().add(useCase.getNotificationsReq(req)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .doFinally(() -> lastSync.set(getDataManager().getLastSync()))
                    .subscribe(response -> {
                        if (response.getApiError().getErrorVal() == ApiError.ERROR_CODE.OK) {
                            getResponseMutableLiveData().postValue(Response.success(response));
                            if (response.getAvailableRecords() > getDataManager().getCacheNotificationCount()) {
                                getAllNotifications();
                            }
                        } else {
                            getResponseMutableLiveData().postValue(Response.error(new Exception(new CustomException(response.getApiError().getErrorMessage()))));
                        }
                    }, throwable -> getResponseMutableLiveData().postValue(Response.error(throwable))));
        }
    }

}
