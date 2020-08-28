package com.gipl.notifyme.domain;

import com.gipl.notifyme.R;
import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.data.model.api.notification.GetNotificationRes;
import com.gipl.notifyme.data.model.api.notification.GetNotificationsReq;
import com.gipl.notifyme.data.model.api.notification.Notification;
import com.gipl.notifyme.uility.AppUtility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Single;

public class NotificationUseCase extends UseCase {

    public NotificationUseCase(DataManager dataManager) {
        super(dataManager);
    }

    public Single<GetNotificationRes> getNotificationsReq(GetNotificationsReq req) {
        return dataManager.getNotifications(req).map(getNotificationRes -> {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.getDefault());
            dataManager.setLastSync(simpleDateFormat.format(calendar.getTime()));

            SimpleDateFormat onlyDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
            SimpleDateFormat onlyTime = new SimpleDateFormat("hh:mm a", Locale.getDefault());

            long daysInMilli = 1000 * 60 * 60 * 24;


            for (Notification notification :
                    getNotificationRes.getNotifications()) {
                if (AppUtility.LINK_TYPE.IMAGE.equalsIgnoreCase(notification.getLinkType())
                        || AppUtility.LINK_TYPE.VIDEO.equalsIgnoreCase(notification.getLinkType())
                        || AppUtility.LINK_TYPE.PDF.equalsIgnoreCase(notification.getLinkType())) {
                    notification.setResId(R.drawable.ic_attchment);
                } else {
                    if (notification.getMessage().contains("http://"))
                        notification.setResId(R.drawable.ic_link);
                }
                Date notiDate = simpleDateFormat.parse(notification.getNotificationDate());
                float timeDiff = calendar.getTime().getTime() - notiDate.getTime();
                if (timeDiff == daysInMilli) {
                    notification.setDisplayDate(onlyTime.format(notiDate));
                } else
                    notification.setDisplayDate(onlyDate.format(notiDate));

            }
            return getNotificationRes;
        });
    }
}
