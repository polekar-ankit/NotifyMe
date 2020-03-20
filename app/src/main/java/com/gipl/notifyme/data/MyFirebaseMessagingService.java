package com.gipl.notifyme.data;

import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.gipl.notifyme.R;
import com.gipl.notifyme.uility.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by naresh on 07-Jun-2019.
 * Updated by Anuj on 20-03-2020
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        //Log.d(TAG, "new token generated:" + s);
        //dataManager.setFirebaseRegToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "Firebase push notification received.");

        Map<String, String> mapData = remoteMessage.getData();
        if (mapData != null) {
            String title = mapData.get("title");
            String message = mapData.get("message");

            if (title != null && message != null) {
                String channelId = getApplicationContext().getString(R.string.notification_channel_id_default);
                //Intent intent = new Intent(this, SplashScreenActivity.class);
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationUtils.sendNotification(getApplicationContext(), channelId, title, message, pendingIntent, true);
            }
        }
    }
}
