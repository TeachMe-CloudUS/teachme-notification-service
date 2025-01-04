package us.cloud.teachme.notification_service.presentation.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserDeletedEvent extends DomainEvent {

    private String id;
    private String email;
    private String eventName;
}
