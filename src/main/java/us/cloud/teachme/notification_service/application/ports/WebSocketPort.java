package us.cloud.teachme.notification_service.application.ports;

import us.cloud.teachme.notification_service.application.dto.NotificationContent;

public interface WebSocketPort {

    void sendNotification(NotificationContent notification);
}
