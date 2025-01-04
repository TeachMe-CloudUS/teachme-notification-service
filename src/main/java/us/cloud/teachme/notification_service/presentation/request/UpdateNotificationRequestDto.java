package us.cloud.teachme.notification_service.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request DTO for notification a notification, containing all necessary details. Note: Notifications are not directly created through a POST call, as they are generated via Kafka events. This class is included for completeness.")
public class UpdateNotificationRequestDto {

    @Schema(description = "Title of the notification", example = "New Assignment Posted")
    private String title;

    @Schema(description = "A short preview text for the notification", example = "You have a new assignment due next week.")
    private String previewText;

    @Schema(description = "Full notification message content", example = "Your instructor has posted a new assignment in Introduction to Programming.")
    private String message;

    @Schema(description = "Type of the notification, e.g., info, warning, etc.", example = "info")
    private String type;

    @Schema(description = "Indicates whether the notification has been read", example = "false")
    private boolean read;
}
