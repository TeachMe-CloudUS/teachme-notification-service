package us.cloud.teachme.notification_service.infrastructure.azure;

import org.springframework.stereotype.Service;
import us.cloud.teachme.notification_service.application.ports.AzureFunctionNotifier;
import us.cloud.teachme.notification_service.domain.Notification;

@Service
public class AzureNotificationService implements AzureFunctionNotifier {

    @Override
    public void notify(Notification notification) {

    }
}
