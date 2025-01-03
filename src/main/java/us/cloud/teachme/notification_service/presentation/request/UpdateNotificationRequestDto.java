package us.cloud.teachme.notification_service.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateNotificationRequestDto {

    private String title;

    private String previewText;

    private String message;

    private String type;

    private boolean read;
}
