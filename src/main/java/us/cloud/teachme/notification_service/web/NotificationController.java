package us.cloud.teachme.notification_service.web;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import us.cloud.teachme.notification_service.application.dto.NotificationsInfo;
import us.cloud.teachme.notification_service.domain.Notification;
import us.cloud.teachme.notification_service.infrastructure.persistence.MongoNotificationRepository;
import us.cloud.teachme.notification_service.web.request.CreateNotificationRequestDto;
import us.cloud.teachme.notification_service.web.request.UpdateNotificationRequestDto;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "APIs for Managing Notifications")
@SecurityRequirement(name = "Authorization")
public class NotificationController {

    private static final Integer DEFAULT_MAX = 10;
    private final SimpMessagingTemplate messagingTemplate;
    private final MongoNotificationRepository repository;

    @GetMapping
    @Operation(summary = "Get notifications for the logged-in user", description = "Fetches notifications for the currently authenticated user, sorted by timestamp.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved notifications")
    @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid")
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
    @Operation(summary = "Get a specific notification", description = "Fetches the notification by its ID for the currently authenticated user.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the notification")
    @ApiResponse(responseCode = "404", description = "Notification not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid")
    public Notification getNotification(@PathVariable String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found"));
    }

    @GetMapping("/info")
    @Operation(summary = "Get notification summary information", description = "Fetches aggregated information about the user's notifications, including total messages, unread count, and recent notifications.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved notification summary")
    @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid")
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
    @Operation(summary = "Mark a notification as read", description = "Marks a specific notification as read by updating its status.")
    @ApiResponse(responseCode = "200", description = "Successfully marked notification as read")
    @ApiResponse(responseCode = "404", description = "Notification not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid")
    public void readMessage(@RequestParam("id") String id) {
        var notification = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found")
        );
        notification.setRead(true);
        repository.save(notification);
    }

    @PostMapping
    @Operation(summary = "Create a new notification", description = "Adds a new notification for a user.")
    @ApiResponse(responseCode = "201", description = "Notification successfully created")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid")
    public Notification createNotification(@RequestBody CreateNotificationRequestDto createNotificationRequest) {
        var notification = Notification.builder()
                .userId(createNotificationRequest.getUserId())
                .title(createNotificationRequest.getTitle())
                .message(createNotificationRequest.getMessage())
                .type(createNotificationRequest.getType())
                .timestamp(createNotificationRequest.getTimestamp())
                .build();
        return repository.save(notification);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing notification", description = "Updates the details of an existing notification.")
    @ApiResponse(responseCode = "200", description = "Notification successfully updated")
    @ApiResponse(responseCode = "404", description = "Notification not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid")
    public Notification updateNotification(@PathVariable String id, @RequestBody UpdateNotificationRequestDto updatedNotification) {
        var existingNotification = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found"));

        if (updatedNotification.getTitle() != null) {
            existingNotification.setTitle(updatedNotification.getTitle());
        }
        if (updatedNotification.getMessage() != null) {
            existingNotification.setMessage(updatedNotification.getMessage());
        }
        if (updatedNotification.getType() != null) {
            existingNotification.setType(updatedNotification.getType());
        }
        existingNotification.setRead(updatedNotification.isRead());

        return repository.save(existingNotification);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a notification", description = "Removes a notification by its ID.")
    @ApiResponse(responseCode = "200", description = "Notification successfully deleted")
    @ApiResponse(responseCode = "404", description = "Notification not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid")
    public void deleteNotification(@PathVariable String id) {
        var existingNotification = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found"));
        repository.delete(existingNotification);
    }

    @GetMapping("/health")
    @Operation(summary = "Check the notification service health", description = "Sends a health check response to the user via a WebSocket queue.")
    @ApiResponse(responseCode = "200", description = "Health check success")
    @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid")
    public void checkHealth(@RequestParam("id") String id) {
        String destination = String.format("%s%s%s", "/queue/", id, "/notifications");
        String response = "{\"status\":\"UP\"}";
        messagingTemplate.convertAndSend(destination, response);
    }
}