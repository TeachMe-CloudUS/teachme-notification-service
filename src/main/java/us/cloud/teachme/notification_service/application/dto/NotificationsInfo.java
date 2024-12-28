package us.cloud.teachme.notification_service.application.dto;

import lombok.Data;
import us.cloud.teachme.notification_service.domain.Notification;

import java.util.List;

@Data
public class NotificationsInfo {

    private int numberOfUnreadMessages;
    private int numberOfMessages;
    private List<Notification> recentNotifications;
}
