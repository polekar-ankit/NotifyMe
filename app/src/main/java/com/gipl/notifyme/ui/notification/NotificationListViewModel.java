package com.gipl.notifyme.ui.notification;

import androidx.databinding.ObservableField;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.data.model.api.ApiError;
import com.gipl.notifyme.data.model.api.notification.GetNotificationsReq;
import com.gipl.notifyme.domain.NotificationUseCase;
import com.gipl.notifyme.exceptions.CustomException;
import com.gipl.notifyme.ui.base.BaseViewModel;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

import io.reactivex.functions.Action;

public class NotificationListViewModel extends BaseViewModel {
    private NotificationUseCase useCase;
    private ObservableField<String> lastSync = new ObservableField<>("");

    public NotificationListViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        useCase = new NotificationUseCase(dataManager);
        lastSync.set(dataManager.getLastSync());
    }

    public ObservableField<String> getLastSync() {
        return lastSync;
    }

    public void getAllNotifications() {
        getResponseMutableLiveData().setValue(Response.loading());
        GetNotificationsReq req = new GetNotificationsReq();
        req.setStart(0);
        req.setCount(10);
        req.setEmpCode(getDataManager().getEmpCode());
        getCompositeDisposable().add(useCase.getNotificationsReq(req)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        lastSync.set(getDataManager().getLastSync());
                    }
                })
                .subscribe(response -> {
                    if (response.getApiError().getErrorVal() == ApiError.ERROR_CODE.OK) {
                        getResponseMutableLiveData().postValue(Response.success(response));
                    } else {
                        getResponseMutableLiveData().postValue(Response.error(new Exception(new CustomException(response.getApiError().getErrorMessage()))));
                    }
                }, throwable -> getResponseMutableLiveData().postValue(Response.error(throwable))));
    }
}
