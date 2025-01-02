package us.cloud.teachme.notification_service.infrastructure.azure;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
class EmailRequest {

    private String to;

    private String subject;

    private String body;
}
