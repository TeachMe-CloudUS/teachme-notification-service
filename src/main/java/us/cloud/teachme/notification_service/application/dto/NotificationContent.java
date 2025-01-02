package us.cloud.teachme.notification_service.application.dto;

import lombok.Builder;
import us.cloud.teachme.notification_service.domain.Notification;

import java.time.Instant;

@Builder
public record NotificationContent(
        String userId,
        String title,
        String previewText,
        String message,
        String type,
        Instant timestamp
) {
    public static NotificationContent create(Notification notification) {
        return NotificationContent.builder()
                .userId(notification.getUserId())
                .title(notification.getTitle())
                .previewText(notification.getPreviewText())
                .message(notification.getMessage())
                .type(notification.getType())
                .timestamp(notification.getTimestamp())
                .build();
    }
}
