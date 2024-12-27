package us.cloud.teachme.notification_service.application.ports;

import us.cloud.teachme.notification_service.domain.Notification;

public interface AzureFunctionNotifier {

    void notify(Notification notification);
}
