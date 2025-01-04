package us.cloud.teachme.notification_service.presentation.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class DomainEvent {

    protected String eventName;
}
