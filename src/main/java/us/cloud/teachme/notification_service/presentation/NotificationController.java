package us.cloud.teachme.notification_service.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import us.cloud.teachme.notification_service.application.dto.NotificationsInfo;
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

    @GetMapping("/info")
    public NotificationsInfo getNotificationInfo(@RequestParam("id") String id) {
        var info = new NotificationsInfo();
        var allNotifications = repository.findAllByUserId(id);
        info.setNumberOfMessages(allNotifications.size());
        info.setRecentNotifications(allNotifications.subList(0, DEFAULT_MAX));
        info.setNumberOfUnreadMessages(
                (int) allNotifications.stream().filter((n) -> !n.isRead()).count()
        );
        return info;
    }

    @PutMapping("/read")
    public void readMessage(@RequestParam("id") String id) {
        repository.findById(id).ifPresent(notification -> {
            notification.setRead(true);
            repository.save(notification);
        });
    }

    @GetMapping("/health")
    public void checkHealth(@RequestParam("id") String id) {
        String destination = String.format("%s%s%s", "/queue/", id, "/notifications");
        String response = "{\"status\":\"UP\"}";
        messagingTemplate.convertAndSend(destination, response);
    }
}