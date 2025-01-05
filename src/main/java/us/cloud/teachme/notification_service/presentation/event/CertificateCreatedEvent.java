package us.cloud.teachme.notification_service.presentation.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CertificateCreatedEvent extends DomainEvent {

    private String userId;
    private String eventName;
    private String courseName;
    private String studentName;
    private String studentSurname;
    private String blobUrl;
}
