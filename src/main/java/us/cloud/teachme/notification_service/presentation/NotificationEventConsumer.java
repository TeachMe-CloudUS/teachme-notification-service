package us.cloud.teachme.notification_service.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import us.cloud.teachme.notification_service.application.dto.NotificationContent;
import us.cloud.teachme.notification_service.application.service.NotificationTemplateService;
import us.cloud.teachme.notification_service.domain.Notification;
import us.cloud.teachme.notification_service.domain.event.StudentCreatedEvent;
import us.cloud.teachme.notification_service.infrastructure.persistence.MongoNotificationRepository;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationEventConsumer {

    private final NotificationTemplateService templateService;
    private final SimpMessagingTemplate messagingTemplate;
    private final MongoNotificationRepository repository;
    private final ObjectMapper mapper;

    @KafkaListener(topics = "student-service.student.created")
    public void consume(@Payload String message) {
        try {
            StudentCreatedEvent event = mapper.readValue(message, StudentCreatedEvent.class);

            var entity = Notification.builder()
                    .title("Welcome to TeachMe!")
                    .userId(event.getUserId())
                    .message(templateService.generateStudentWelcomeMessage(event))
                    .type("STUDENT_CREATED")
                    .timestamp(event.getTimestamp())
                    .build();

            var content = NotificationContent.builder()
                    .title("Welcome to TeachMe!")
                    .message(templateService.generateStudentWelcomeMessage(event))
                    .type("STUDENT_CREATED")
                    .timestamp(event.getTimestamp())
                    .build();

            repository.save(entity);

            String destination = String.format("%s%s%s", "/queue/", event.getUserId(), "/notifications");
            messagingTemplate.convertAndSend(destination, content);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}