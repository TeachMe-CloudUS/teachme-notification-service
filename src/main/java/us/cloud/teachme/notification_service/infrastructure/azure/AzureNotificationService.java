package us.cloud.teachme.notification_service.infrastructure.azure;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import us.cloud.teachme.notification_service.application.dto.EmailNotificationContent;
import us.cloud.teachme.notification_service.application.ports.AzureFunctionNotifier;
import us.cloud.teachme.notification_service.application.ports.StudentServiceClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class AzureNotificationService implements AzureFunctionNotifier {

    private final StudentServiceClient userServiceClient;
    private final RestClient azureFunctionClient;

    @Override
    public void notify(EmailNotificationContent notification) {
        log.info("Received notification: {}", notification);

        var user = userServiceClient.getStudent(notification.userId());
        if (user == null) {
            log.error("User not found for userId: {}", notification.userId());
            return;
        }

        var email = user.getContactInformation().getEmail();

        triggerFunction(notification, email);
    }

    @Override
    public void notify(EmailNotificationContent notification, String email) {
        triggerFunction(notification, email);
    }

    private void triggerFunction(EmailNotificationContent notification, String email) {
        try {
            var body = EmailRequest.builder()
                    .to(email)
                    .subject(notification.title())
                    .body(notification.emailContent())
                    .build();

            String requestBody = new ObjectMapper().writeValueAsString(body);

            var response = azureFunctionClient.post()
                    .uri("/api/sendMail")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .toEntity(String.class);

            log.info("Response: {}", response);
        } catch (Exception e) {
            log.error("Failed to send notification to: {}", email, e);
        }
    }
}
