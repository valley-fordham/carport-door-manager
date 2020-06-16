package com.glenfordham.carportopener.gui.notifications.channels;

import android.app.Notification;
import android.app.NotificationManager;
import android.graphics.Color;

public enum Channel {
    FOREGROUND_SERVICE(
            "DOOR_SERVICE",
            31,
            "Door Watch Service",
            "Displays a notification informing user that the Door Watch Service is running.",
            Notification.CATEGORY_SERVICE,
            NotificationManager.IMPORTANCE_NONE,
            Notification.VISIBILITY_SECRET,
            false,
            false,
            Color.TRANSPARENT,
            false,
            false,
            false),

    DOOR_STATUS(
            "DOOR_STATUS",
            27,
            "Door Status Notifications",
            "Displays notifications whenever the carport door is open, or an error occurs when detecting the door status.",
            Notification.CATEGORY_STATUS,
            NotificationManager.IMPORTANCE_MAX,
            Notification.VISIBILITY_PUBLIC,
            true,
            true,
            Color.CYAN,
            true,
            true,
            true),

    DOOR_ERROR(
            "DOOR_ERROR",
            35,
            "Door Error Notifications",
            "Displays notifications whenever the carport door service cannot be reached.",
            Notification.CATEGORY_STATUS,
            NotificationManager.IMPORTANCE_MAX,
            Notification.VISIBILITY_PUBLIC,
            true,
            true,
            Color.RED,
            true,
            true,
            true);

    private String channelId;
    private int id;
    private String name;
    private String description;
    private String category;
    private int importance;
    private int visibility;
    private boolean lightsEnabled;
    private boolean vibrationEnabled;
    private int lightColour;
    private boolean timestampEnabled;
    private boolean autoCancelEnabled;
    private boolean notificationDotEnabled;

    Channel(String channelId, int id, String name, String description, String category, int importance, int visibility, boolean lightsEnabled, boolean vibrationEnabled, int lightColour, boolean timestampEnabled, boolean autoCancelEnabled, boolean notificationDotEnabled) {
        this.channelId = channelId;
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.importance = importance;
        this.visibility = visibility;
        this.lightsEnabled = lightsEnabled;
        this.vibrationEnabled = vibrationEnabled;
        this.lightColour = lightColour;
        this.timestampEnabled = timestampEnabled;
        this.autoCancelEnabled = autoCancelEnabled;
        this.notificationDotEnabled = notificationDotEnabled;
    }

    public String getChannelId() {
        return channelId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public int getImportance() {
        return importance;
    }

    public int getVisibility() {
        return visibility;
    }

    public boolean isLightsEnabled() {
        return lightsEnabled;
    }

    public boolean isVibrationEnabled() {
        return vibrationEnabled;
    }

    public int getLightColour() {
        return lightColour;
    }

    public boolean isTimestampEnabled() {
        return timestampEnabled;
    }

    public boolean isAutoCancelEnabled() {
        return autoCancelEnabled;
    }

    public boolean isNotificationDotEnabled() {
        return notificationDotEnabled;
    }
}
