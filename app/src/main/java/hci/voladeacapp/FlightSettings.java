package hci.voladeacapp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bianchi on 19/11/16.
 */

public class FlightSettings {
    private boolean notificationsActive;
    private Map<NotificationCategory, Boolean> notificationSettings;

    public FlightSettings() {
        notificationsActive = true;
        notificationSettings = new HashMap<>();

        for (NotificationCategory cat: NotificationCategory.values())
            notificationSettings.put(cat, true);
    }

    public void setAllNotifications(Boolean isActive) {
        notificationsActive = isActive;
    }

    public boolean notificationsActive() {
        return notificationsActive;
    }

    public void enableNotifications() {
        notificationsActive = true;
    }

    public void disableNotifications() {
        notificationsActive = false;
    }

    public boolean isActive(NotificationCategory category) {
        if (!notificationSettings.containsKey(category))
            throw new IllegalArgumentException("Non-existing notification category");
        return notificationSettings.get(category);
    }

    public void setNotification(NotificationCategory category, Boolean isActive) {
        if (!notificationSettings.containsKey(category))
            throw new IllegalArgumentException("Non-existing notification category");
        notificationSettings.put(category, isActive);
    }
}