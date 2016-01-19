package com.water.waterreminder.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by kurayogun on 19/01/16.
 */
public final class NotificationServiceStarterReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationEventReceiver.setupAlarm(context);
    }
}
