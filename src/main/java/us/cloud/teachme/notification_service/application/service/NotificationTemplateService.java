package us.cloud.teachme.notification_service.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.cloud.teachme.notification_service.domain.event.StudentCreatedEvent;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationTemplateService {

    public String generateStudentWelcomeMessage(StudentCreatedEvent event) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        String formattedDate = event.getEnrollmentDate()
                .atZone(ZoneId.systemDefault())
                .format(formatter);

        return """
                <div class="notification-content">
                    <h4>Welcome to TeachMe, %s!</h4>
                    <p>Your student account has been successfully created.</p>
                    <p>Enrollment Date: %s</p>
                    <div class="notification-actions">
                        <a href="/student/dashboard" class="btn btn-primary">Go to Dashboard</a>
                    </div>
                </div>
                """.formatted(
                event.getName() + " " + event.getSurname(),
                formattedDate
        );
    }
}
