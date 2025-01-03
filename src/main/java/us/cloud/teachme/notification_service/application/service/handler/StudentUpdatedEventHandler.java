package us.cloud.teachme.notification_service.application.service.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.cloud.teachme.notification_service.application.dto.NotificationContent;
import us.cloud.teachme.notification_service.application.ports.WebSocketPort;
import us.cloud.teachme.notification_service.domain.Notification;
import us.cloud.teachme.notification_service.domain.event.StudentUpdatedEvent;
import us.cloud.teachme.notification_service.infrastructure.persistence.MongoNotificationRepository;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentUpdatedEventHandler implements KafkaEventHandler {

    private static final String TOPIC = "student-service.student.updated";

    private final MongoNotificationRepository repository;
    private final ObjectMapper mapper;
    private final WebSocketPort webSocketPort;

    @Override
    public boolean canHandle(String topic) {
        return TOPIC.equals(topic);
    }

    @Override
    public void handle(String message) {
        try {
            StudentUpdatedEvent event = mapper.readValue(message, StudentUpdatedEvent.class);

            var entity = Notification.builder()
                    .title("Successfully updated profile!")
                    .userId(event.getUserId())
                    .message("""
                            <div class="notification-content">
                                           <p>Your profile was successfully updated</p>
                                       </div>
                            """)
                    .previewText("Your profile was successfully updated")
                    .type("STUDENT_UPDATED")
                    .timestamp(Instant.now())
                    .build();


            var content = NotificationContent.create(entity);

            repository.save(entity);

            webSocketPort.sendNotification(content);
            log.info("Notification successfully sent via WebSocket.");
        } catch (JsonProcessingException e) {
            log.error("Error occurred during message handling: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
