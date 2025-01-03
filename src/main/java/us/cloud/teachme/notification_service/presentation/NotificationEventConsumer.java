package us.cloud.teachme.notification_service.presentation;

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

    @KafkaListener(topics = {"student-service.student.created"})
    public void consume(@Payload String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        eventProcessor.process(message, topic);
    }
}