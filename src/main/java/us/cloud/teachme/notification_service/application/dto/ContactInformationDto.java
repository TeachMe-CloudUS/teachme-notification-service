package us.cloud.teachme.notification_service.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactInformationDto {

    private String name;

    private String surname;

    private String email;

    private String phoneNumber;

    private String country;
}
