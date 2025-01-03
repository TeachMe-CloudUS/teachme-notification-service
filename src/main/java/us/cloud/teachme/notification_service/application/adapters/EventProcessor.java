package us.cloud.teachme.notification_service.application.adapters;

public interface EventProcessor {

    void process(String message, String topic);
}
