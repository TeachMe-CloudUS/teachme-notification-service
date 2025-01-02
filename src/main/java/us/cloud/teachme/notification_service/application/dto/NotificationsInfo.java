package us.cloud.teachme.notification_service.application.dto;

import lombok.Data;
import us.cloud.teachme.notification_service.domain.Notification;

import java.util.List;

/**
 * Represents summary information about a user's notifications.
 * This includes the total count of messages, the count of unread messages,
 * and a list of recent notifications.
 */
@Data
public class NotificationsInfo {

    /**
     * Represents the count of messages that have not yet been read by the user.
     * This value is used to indicate the number of unread notifications in the system.
     */
    private int numberOfUnreadMessages;
    /**
     * Represents the total number of messages associated with the user's notifications.
     * This value includes both read and unread notifications and provides
     * an overview of the total notifications count.
     */
    private int numberOfMessages;
    /**
     * A list that stores recent notifications for a user.
     * Each entry in the list represents a {@link Notification} object,
     * containing details such as title, message, timestamp, and read status.
     * This field is used to provide an overview of the user's latest notifications,
     * typically displayed in a notification feed or summary interface.
     */
    private List<Notification> recentNotifications;
}
