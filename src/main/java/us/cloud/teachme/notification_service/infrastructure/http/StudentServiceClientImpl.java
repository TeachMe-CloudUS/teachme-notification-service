package us.cloud.teachme.notification_service.infrastructure.http;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import us.cloud.teachme.notification_service.application.dto.StudentDto;
import us.cloud.teachme.notification_service.application.ports.StudentServiceClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceClientImpl implements StudentServiceClient {

    private final RestClient studentServiceClient;

    @Override
    public StudentDto getStudent(String userId) {
        try {
            return studentServiceClient.get()
                    .uri(String.format("/api/v1/students/byUserId/%s", userId))
                    .retrieve()
                    .toEntity(StudentDto.class)
                    .getBody();

        } catch (Exception e) {
            log.error("Failed to get user for userId: {}", userId, e);
            return null;
        }
    }
}
