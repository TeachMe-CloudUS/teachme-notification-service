package us.cloud.teachme.notification_service.application.service.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import us.cloud.teachme.notification_service.application.dto.EmailNotificationContent;
import us.cloud.teachme.notification_service.application.dto.NotificationContent;
import us.cloud.teachme.notification_service.application.ports.AzureFunctionNotifier;
import us.cloud.teachme.notification_service.application.ports.WebSocketPort;
import us.cloud.teachme.notification_service.application.service.NotificationTemplateService;
import us.cloud.teachme.notification_service.domain.Notification;
import us.cloud.teachme.notification_service.domain.event.StudentCreatedEvent;
import us.cloud.teachme.notification_service.infrastructure.persistence.MongoNotificationRepository;

@Service
@RequiredArgsConstructor
public class StudentCreatedEventHandler implements KafkaEventHandler {

    private static final String TOPIC = "student-service.student.created";

    private final NotificationTemplateService templateService;
    private final MongoNotificationRepository repository;
    private final ObjectMapper mapper;
    private final WebSocketPort webSocketPort;
    private final AzureFunctionNotifier azureFunctionNotifier;

    @Override
    public boolean canHandle(String topic) {
        return TOPIC.equals(topic);
    }

    @Override
    public void handle(String message) {
        try {
            StudentCreatedEvent event = mapper.readValue(message, StudentCreatedEvent.class);

            var messageTemplate = templateService.generateStudentWelcomeMessage(event);

            var entity = Notification.builder()
                    .title("Welcome to TeachMe!")
                    .userId(event.getUserId())
                    .message(messageTemplate.getContent())
                    .previewText(messageTemplate.getPreviewText())
                    .type("STUDENT_CREATED")
                    .timestamp(event.getTimestamp())
                    .build();

            var content = NotificationContent.create(entity);
            var mailContent = EmailNotificationContent.create(entity, messageTemplate.emailContent);

            repository.save(entity);

            webSocketPort.sendNotification(content);
            azureFunctionNotifier.notify(mailContent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
