package us.cloud.teachme.notification_service.domain.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class DomainEvent {

    protected String eventName;
}
