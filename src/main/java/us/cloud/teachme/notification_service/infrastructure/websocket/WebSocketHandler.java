package us.cloud.teachme.notification_service.infrastructure.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import us.cloud.teachme.notification_service.application.dto.NotificationContent;
import us.cloud.teachme.notification_service.application.ports.WebSocketPort;

@Service
@RequiredArgsConstructor
public class WebSocketHandler implements WebSocketPort {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendNotification(NotificationContent notification) {
        String destination = String.format("%s%s%s", "/queue/", notification.userId(), "/notifications");
        messagingTemplate.convertAndSend(destination, notification);
    }
}
