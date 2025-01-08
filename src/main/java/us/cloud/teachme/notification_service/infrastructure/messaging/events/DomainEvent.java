package us.cloud.teachme.notification_service.infrastructure.messaging.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class DomainEvent {

    protected String eventName;
}
