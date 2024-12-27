package us.cloud.teachme.notification_service.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import us.cloud.teachme.notification_service.domain.Notification;
import us.cloud.teachme.notification_service.infrastructure.persistence.MongoNotificationRepository;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private static final Integer DEFAULT_MAX = 5;
    private final SimpMessagingTemplate messagingTemplate;
    private final MongoNotificationRepository repository;

    @GetMapping
    public List<Notification> getNotifications(
            @RequestParam("id") String id,
            @RequestParam("unread") Boolean unread,
            @RequestParam("max") Integer max
    ) {
        if (Objects.isNull(max)) {
            max = DEFAULT_MAX;
        }

        return repository.findAllByUserId(id).subList(0, max);
    }

    @GetMapping("/health")
    public void checkHealth(@RequestParam("id") String id) {
        String destination = String.format("%s%s%s", "/queue/", id, "/notifications");
        String response = "{\"status\":\"UP\"}";
        messagingTemplate.convertAndSend(destination, response);
    }
}