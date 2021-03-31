package com.gipl.swayam.domain;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.gipl.swayam.R;
import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.data.model.api.notification.GetNotificationRes;
import com.gipl.swayam.data.model.api.notification.GetNotificationsReq;
import com.gipl.swayam.data.model.api.notification.Notification;
import com.gipl.swayam.data.model.db.TNotification;
import com.gipl.swayam.uility.AppUtility;
import com.gipl.swayam.uility.TimeUtility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Single;

public class NotificationUseCase extends UseCase {

    public NotificationUseCase(DataManager dataManager) {
        super(dataManager);
    }

    public LiveData<ArrayList<Notification>> getNotificationList() {
        return Transformations.map(dataManager.getNotificationList(),
                input -> {
                    Calendar calendar = Calendar.getInstance();
//                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.getDefault());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TimeUtility.DB_FORMAT, Locale.getDefault());
                    dataManager.setLastSync(simpleDateFormat.format(calendar.getTime()));

                    SimpleDateFormat onlyDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                    SimpleDateFormat onlyTime = new SimpleDateFormat("hh:mm a", Locale.getDefault());

                    long daysInMilli = 1000 * 60 * 60 * 24;


                    for (Notification notification :
                            input) {
                        if (AppUtility.LINK_TYPE.IMAGE.equalsIgnoreCase(notification.getLinkType())
                                || AppUtility.LINK_TYPE.VIDEO.equalsIgnoreCase(notification.getLinkType())
                                || AppUtility.LINK_TYPE.PDF.equalsIgnoreCase(notification.getLinkType())) {
                            notification.setResId(R.drawable.ic_attchment);
                        } else {
                            if (notification.getMessage().contains("http://"))
                                notification.setResId(R.drawable.ic_link);
                        }
                        Date notiDate;
                        try {
                            notiDate = simpleDateFormat.parse(notification.getNotificationDate());
                            float timeDiff = calendar.getTime().getTime() - notiDate.getTime();

                            if (timeDiff == daysInMilli) {
                                notification.setDisplayDate(onlyTime.format(notiDate));
                            } else
                                notification.setDisplayDate(onlyDate.format(notiDate));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }
                    return (ArrayList<Notification>) input;
                });
    }

    public Single<GetNotificationRes> getNotificationsReq(GetNotificationsReq req) {
        return dataManager.getNotifications(req).map(getNotificationRes -> {
            if (getNotificationRes.getNotifications() != null) {
                String cacheTime = TimeUtility.getCurrentTimeInDbFormat();
                for (Notification notification :
                        getNotificationRes.getNotifications()) {
                    dataManager.insertNotification(new TNotification(notification, cacheTime));
                }
            }
            dataManager.setTotalNotificationCache(dataManager.getTotalNotificationCacheRm());
            return getNotificationRes;
        });
    }
}
