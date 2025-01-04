package us.cloud.teachme.notification_service.application.service.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import us.cloud.teachme.notification_service.application.dto.NotificationContent;
import us.cloud.teachme.notification_service.application.ports.WebSocketPort;
import us.cloud.teachme.notification_service.domain.Notification;
import us.cloud.teachme.notification_service.presentation.event.ActivationMailSentEvent;
import us.cloud.teachme.notification_service.infrastructure.persistence.MongoNotificationRepository;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ActivationMailSentEventHandler implements KafkaEventHandler {

    private static final String TOPIC = "auth-service.user.created";

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
            var event = mapper.readValue(message, ActivationMailSentEvent.class);

            var entity = Notification.builder()
                    .title("Please Activate Your TeachMe Account!")
                    .userId(event.getId())
                    .message("""
                            <div class="notification-content">
                                <h4>Activate Your TeachMe Account!</h4>
                                <p>Thank you for signing up! We’ve sent an activation link to your email address: <strong>%s</strong>.</p>
                                <p>To get started, please activate your account by clicking the link in the email we just sent.</p>
                                <p>If you didn’t receive the email, check your spam folder.</p>
                                <p>Need assistance? Visit our <a href="/student/help">Help Center</a>.</p>
                            </div>
                            """.formatted(event.getEmail()))
                    .previewText("Thank you for signing up! We’ve sent an activation link to your email address: %s".formatted(event.getEmail()))
                    .type("ACTIVATION_MAIL_SENT")
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
