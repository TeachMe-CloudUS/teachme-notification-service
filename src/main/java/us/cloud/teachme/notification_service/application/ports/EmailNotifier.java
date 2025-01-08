package us.cloud.teachme.notification_service.application.ports;

import us.cloud.teachme.notification_service.application.dto.EmailNotificationContent;

public interface EmailNotifier {

    void notify(EmailNotificationContent notification);

    void notify(EmailNotificationContent notification, String email);
}
