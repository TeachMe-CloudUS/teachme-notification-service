package us.cloud.teachme.notification_service.application.dto;

import lombok.Builder;
import us.cloud.teachme.notification_service.domain.Notification;

@Builder
public record EmailNotificationContent(
        String userId,
        String title,
        String emailContent
) {
    public static EmailNotificationContent create(Notification notification, String emailContent) {
        return EmailNotificationContent.builder()
                .userId(notification.getUserId())
                .title(notification.getTitle())
                .emailContent(emailContent)
                .build();
    }
}
