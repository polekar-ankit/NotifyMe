package com.gipl.notifyme.domain;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.data.model.api.notification.GetNotificationRes;
import com.gipl.notifyme.data.model.api.notification.GetNotificationsReq;
import com.gipl.notifyme.data.model.api.sendotp.SendOTPReq;
import com.gipl.notifyme.data.model.api.sendotp.SendOtpRes;
import com.gipl.notifyme.data.model.api.verifyotp.VerifyOtpReq;
import com.gipl.notifyme.data.model.api.verifyotp.VerifyOtpRsp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.reactivex.Single;
import io.reactivex.functions.Function;

public class NotificationUseCase extends UseCase {

    public NotificationUseCase(DataManager dataManager) {
        super(dataManager);
    }

    public Single<GetNotificationRes> getNotificationsReq(GetNotificationsReq req) {
        return dataManager.getNotifications(req).map(getNotificationRes -> {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.getDefault());
            dataManager.setLastSync(simpleDateFormat.format(calendar.getTime()));
            return getNotificationRes;
        });
    }
}
