package us.cloud.teachme.notification_service.application.service.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import us.cloud.teachme.notification_service.application.dto.NotificationContent;
import us.cloud.teachme.notification_service.application.ports.WebSocketPort;
import us.cloud.teachme.notification_service.domain.Notification;
import us.cloud.teachme.notification_service.presentation.event.CertificateCreatedEvent;
import us.cloud.teachme.notification_service.infrastructure.persistence.MongoNotificationRepository;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CertificateCreatedEventHandler implements KafkaEventHandler {

    private static final String TOPIC = "certification-service.certificate.created";

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
            var event = mapper.readValue(message, CertificateCreatedEvent.class);

            var entity = Notification.builder()
                    .title("Congratulations!")
                    .userId(event.getUserId())
                    .message("""
                            <div class="notification-content">
                                <h4>Congratulations, %s!</h4>
                                <p>We're excited to share that you've successfully completed your course "%s" and your certificate is ready for download!</p>
                                <p>This is a significant milestone, and we're proud to celebrate your achievement with you.</p>
                                <p>You can download your certificate <a href="%s" target="_blank">here</a>.</p>
                                <p>If you have any questions or need assistance, feel free to visit our <a href="/student/help">Help Center</a>.</p>
                                <p>Once again, congratulations on your achievement! Keep up the great work! 🎉</p>
                            </div>
                            """.formatted(event.getStudentName() + " " + event.getStudentSurname(), event.getCourseName(), event.getBlobUrl()))
                    .previewText("Congratulations, %s! We're excited to share that you've successfully completed your course \"%s\" and your certificate is ready for download!"
                            .formatted(event.getStudentName() + " " + event.getStudentSurname(), event.getCourseName()))
                    .type("CERTIFICATE_CREATED")
                    .timestamp(Instant.now())
                    .build();

            var content = NotificationContent.create(entity);

            repository.save(entity);

            webSocketPort.sendNotification(content);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
