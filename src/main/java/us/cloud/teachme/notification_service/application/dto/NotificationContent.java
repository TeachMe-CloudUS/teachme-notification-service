package us.cloud.teachme.notification_service.application.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record NotificationContent(
        String title,
        String message,
        String type,
        Instant timestamp
) {
}
