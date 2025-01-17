package us.cloud.teachme.notification_service.infrastructure.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import us.cloud.teachme.notification_service.application.adapters.EventProcessor;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationEventConsumer {

    private final EventProcessor eventProcessor;

    @KafkaListener(topics = {
            "student-service.student.created",
            "student-service.student.updated",
            "certification-service.certificate.created",
            "auth-service.user.created",
    })
    public void consume(@Payload String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("Received message: {} from topic: {}", message, topic);
        eventProcessor.process(message, topic);
    }
}