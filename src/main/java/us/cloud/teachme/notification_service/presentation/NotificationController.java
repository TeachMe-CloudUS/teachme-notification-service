package us.cloud.teachme.notification_service.presentation;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @AuthenticationPrincipal Claims claims,
            @RequestParam("unread") Boolean unread,
            @RequestParam("max") Integer max
    ) {
        if (Objects.isNull(max)) {
            max = DEFAULT_MAX;
        }

        return repository.findAllByUserId(claims.getSubject())
                .stream()
                .sorted((n1, n2) -> n2.getTimestamp().compareTo(n1.getTimestamp()))
                .toList()
                .subList(0, max);
    }

    @GetMapping("/{id}")
    public Notification getNotification(@PathVariable String id) {
        return repository.findById(id).orElseThrow();
    }

    @GetMapping("/info")
    public NotificationsInfo getNotificationInfo(@AuthenticationPrincipal Claims claims) {
        var info = new NotificationsInfo();
        var allNotifications = repository.findAllByUserId(claims.getSubject())
                .stream()
                .sorted((n1, n2) -> n2.getTimestamp().compareTo(n1.getTimestamp()))
                .toList();
        info.setNumberOfMessages(allNotifications.size());
        info.setRecentNotifications(allNotifications.subList(0, Math.min(DEFAULT_MAX, allNotifications.size())));
        info.setNumberOfUnreadMessages(
                (int) allNotifications.stream().filter((n) -> !n.isRead()).count()
        );
        return info;
    }

    @PutMapping("/read")
    public void readMessage(@RequestParam("id") String id) {
        var notification = repository.findById(id).orElseThrow();
        notification.setRead(true);
        repository.save(notification);
    }

    @GetMapping("/health")
    public void checkHealth(@RequestParam("id") String id) {
        String destination = String.format("%s%s%s", "/queue/", id, "/notifications");
        String response = "{\"status\":\"UP\"}";
        messagingTemplate.convertAndSend(destination, response);
    }
}