package com.gipl.notifyme.ui.notification;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.data.model.api.ApiError;
import com.gipl.notifyme.data.model.api.notification.GetNotificationsReq;
import com.gipl.notifyme.domain.NotificationUseCase;
import com.gipl.notifyme.exceptions.CustomException;
import com.gipl.notifyme.ui.base.BaseViewModel;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

public class NotificationListViewModel extends BaseViewModel {
    NotificationUseCase useCase;
    public NotificationListViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        useCase = new NotificationUseCase(dataManager);
    }

    public void getAllNotifications() {
        getResponseMutableLiveData().setValue(Response.loading());
        GetNotificationsReq req = new GetNotificationsReq();
        req.setStart(0);
        req.setCount(10);
        // TODO: 21-03-2020 Get employee code from Logged in user
        req.setEmpCode("G028");
        getCompositeDisposable().add(useCase.getNotificationsReq(req)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    if (response.getApiError().getErrorVal() == ApiError.ERROR_CODE.OK) {
                        getResponseMutableLiveData().postValue(Response.success(response));
                    } else {
                        getResponseMutableLiveData().postValue(Response.error(new Exception(new CustomException(response.getApiError().getErrorMessage()))));
                    }
                }, throwable -> getResponseMutableLiveData().postValue(Response.error(throwable))));
    }
}
