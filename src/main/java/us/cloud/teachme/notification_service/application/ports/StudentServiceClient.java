package us.cloud.teachme.notification_service.application.ports;

import us.cloud.teachme.notification_service.application.dto.StudentDto;

public interface StudentServiceClient {

    StudentDto getStudent(String userId);
}
