package us.cloud.teachme.notification_service.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import us.cloud.teachme.notification_service.application.adapters.EventProcessor;
import us.cloud.teachme.notification_service.application.service.handler.KafkaEventHandler;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KafkaEventProcessor implements EventProcessor {

    private final List<KafkaEventHandler> eventHandler;

    @Override
    public void process(String message, String topic) {
        eventHandler.stream().filter(
                handler -> handler.canHandle(topic))
                .forEach(handler -> handler.handle(message));
    }
}
