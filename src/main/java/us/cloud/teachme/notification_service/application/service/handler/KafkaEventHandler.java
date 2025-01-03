package us.cloud.teachme.notification_service.application.service.handler;

public interface KafkaEventHandler {

    boolean canHandle(String topic);

    void handle(String message);
}
