package us.cloud.teachme.notification_service.infrastructure.messaging.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class StudentUpdatedEvent extends DomainEvent {

    private String studentId;
    private String eventName;
    private String userId;
    private String phoneNumber;
    private String plan;
}
