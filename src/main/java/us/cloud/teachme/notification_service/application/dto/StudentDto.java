package us.cloud.teachme.notification_service.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {

    private String id;

    private String userId;

    private ContactInformationDto contactInformation;
}
