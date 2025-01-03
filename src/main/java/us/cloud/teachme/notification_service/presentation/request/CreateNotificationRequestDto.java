package us.cloud.teachme.notification_service.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateNotificationRequestDto {

    private String userId;

    private String title;

    private String previewText;

    private String message;

    private String type;

    private Instant timestamp;

    private boolean read;
}
