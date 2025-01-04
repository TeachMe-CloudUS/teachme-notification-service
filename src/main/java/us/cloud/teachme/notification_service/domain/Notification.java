package us.cloud.teachme.notification_service.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/// Represents a notification entity in the system.
/// This class is used to encapsulate the data related to
/// user notifications, including the content, metadata, and state.
/// An instance of this class is saved in the database
/// and can be fetched to track notification events for users.
/// Key attributes include:
/// - A unique identifier for the notification.
/// - The user to whom the notification is related.
/// - Title and preview text to summarize the notification.
/// - A detailed message for user engagement.
/// - The type of notification (e.g., activation email, system alerts).
/// - Timestamp indicating when the notification was created.
/// - A flag to denote if the notification has been read.
/// This class uses annotations for database persistence
/// and object serialization/deserialization.
@Data
@Builder
@Document
public class Notification {

    @Id
    private String id;

    @Indexed
    private String userId;

    private String title;

    private String previewText;

    private String message;

    private String type;

    private Instant timestamp;

    private boolean read;
}
