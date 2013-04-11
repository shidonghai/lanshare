
package com.nano.lanshare.main.nitification;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.nano.lanshare.R;

public class BackGroundNotification {
    private static final int BACKGROUND_NOTIFICATION_ID = 1;
    private Context mContext;
    private NotificationManager mNotificationManager;

    public BackGroundNotification(Context context) {
        mContext = context;
        mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void showNotify() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.drawable.avatar1)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
        mNotificationManager.notify(BACKGROUND_NOTIFICATION_ID, mBuilder.build());
    }

    public void cancel() {
        mNotificationManager.cancel(BACKGROUND_NOTIFICATION_ID);
    }
}
