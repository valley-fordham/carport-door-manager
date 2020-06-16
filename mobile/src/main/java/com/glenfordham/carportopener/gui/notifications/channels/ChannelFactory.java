package com.glenfordham.carportopener.gui.notifications.channels;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

public class ChannelFactory {

    private Context context;

    public ChannelFactory(Context context) {
        this.context = context;
    }

    /**
     * Creates a new NotificationChannel object representing the passed channel if one doesn't exist, and returns the channel Id
     * @param channel : the channel enum identifier
     * @return : the channel Id, to be used by the NotificationManager
     */
    public String getChannel(Channel channel) {
        NotificationChannel notificationChannel =
                new NotificationChannel(
                        channel.getChannelId(),
                        channel.getName(),
                        channel.getImportance());

        notificationChannel.setDescription(channel.getDescription());
        notificationChannel.enableVibration(channel.isVibrationEnabled());
        notificationChannel.setLockscreenVisibility(channel.getVisibility());
        notificationChannel.enableLights(channel.isLightsEnabled());
        notificationChannel.setLightColor(channel.getLightColour());
        notificationChannel.setImportance(channel.getImportance());
        notificationChannel.setShowBadge(channel.isNotificationDotEnabled());

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);

        return channel.getChannelId();
    }

    // prevent init without argument
    private ChannelFactory() {
    }

}
