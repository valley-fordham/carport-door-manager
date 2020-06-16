package com.glenfordham.carportopener.gui.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import com.glenfordham.carportopener.R;
import com.glenfordham.carportopener.gui.MainScreen;
import com.glenfordham.carportopener.gui.notifications.channels.Channel;
import com.glenfordham.carportopener.gui.notifications.channels.ChannelFactory;

public class Toast {
    private Notification notification;
    private Channel channel;
    private Context context;

    Toast(String title, String message, Channel channel, Context context) {
        // Open up the HomeScreen activity if the notification is pressed
        Intent intent = new Intent(context, MainScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Build notification then create delicious Toast
        this.notification = new Notification.Builder(context.getApplicationContext(),
                new ChannelFactory(context).getChannel(channel))
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.persona_cat)
                .setLargeIcon(BitmapFactory.decodeResource(context.
                                getResources(),
                        R.drawable.persona_cat_2))
                .setCategory(channel.getCategory())
                .setShowWhen(channel.isTimestampEnabled())
                .setContentIntent(pendingIntent)
                .setAutoCancel(channel.isAutoCancelEnabled())
                .build();

        this.channel = channel;
        this.context = context;
    }

    public void sendNotification() {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(channel.getId(), notification);
    }

    Notification asNotification() {
        return notification;
    }

    // Prevent init without arguments
    private Toast() {
    }
}
