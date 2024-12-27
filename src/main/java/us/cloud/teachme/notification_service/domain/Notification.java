package us.cloud.teachme.notification_service.domain;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class Notification {
    private String id;
    private String userId;
    private String title;
    private String message;
    private String type;
    private LocalDateTime timestamp;
    private boolean read;
    private Map<String, Object> metadata;
}
