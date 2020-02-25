package com.example.refresh;

import androidx.core.app.NotificationCompat;

public final class GlobalNotificationBuilder {
    private static NotificationCompat.Builder sGlobalNotificationCompatBuilder = null;

    private GlobalNotificationBuilder() {
    }

    public static void setNotificationCompatBuilderInstance(NotificationCompat.Builder builder) {
        sGlobalNotificationCompatBuilder = builder;
    }

    public static NotificationCompat.Builder getNotificationCompatBuilderInstance() {
        return sGlobalNotificationCompatBuilder;
    }
}
