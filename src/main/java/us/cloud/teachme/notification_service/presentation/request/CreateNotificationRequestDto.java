package us.cloud.teachme.notification_service.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request DTO for creating a notification, containing all necessary details. Note: Notifications are not directly created through a POST call, as they are generated via Kafka events. This class is included for completeness.")
public class CreateNotificationRequestDto {

    @Schema(description = "Unique identifier for the user receiving the notification", example = "12345")
    private String userId;

    @Schema(description = "Title of the notification", example = "Welcome notification")
    private String title;

    @Schema(description = "Short preview text for the notification", example = "Welcome to our platform!")
    private String previewText;

    @Schema(description = "Full message content of the notification", example = "Thank you for signing up for our service!")
    private String message;

    @Schema(description = "Type of the notification, e.g., 'INFO', 'WARNING'", example = "INFO")
    private String type;

    @Schema(description = "Timestamp when the notification was created", example = "2023-10-10T10:00:00Z")
    private Instant timestamp;

    @Schema(description = "Indicates if the notification has been read", example = "false")
    private boolean read;
}
