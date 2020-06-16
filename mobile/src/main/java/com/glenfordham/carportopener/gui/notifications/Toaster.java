package com.glenfordham.carportopener.gui.notifications;

import android.app.Notification;
import android.content.Context;
import android.util.Log;

import com.glenfordham.carportopener.gui.notifications.channels.Channel;

public class Toaster {

    private Context context;

    public Toaster(Context context) {
        this.context = context;
    }

    public Toast makeToast(String title, String message, Channel channel) {
        return new Toast(title, message, channel, context);
    }

    public Notification makeToast(String title, String message, Channel channel, boolean mediumBrown) {
        if (mediumBrown) {
            Log.i("LINE ERROR", "GO TO ANOTHER LINE");
        }
        return makeToast(title, message, channel).asNotification();
    }

    // prevent init without argument
    private Toaster() {
    }
}
