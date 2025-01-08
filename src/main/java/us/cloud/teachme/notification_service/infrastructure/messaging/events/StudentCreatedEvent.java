package us.cloud.teachme.notification_service.infrastructure.messaging.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class StudentCreatedEvent extends DomainEvent {

    private String studentId;
    private String userId;
    private String eventName;
    private String name;
    private String surname;
    private Instant timestamp;
}
