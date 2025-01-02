package us.cloud.teachme.notification_service.application.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.cloud.teachme.notification_service.domain.event.StudentCreatedEvent;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationTemplateService {

    public NotificationTemplate generateStudentWelcomeMessage(StudentCreatedEvent event) {
        String fullName = event.getName() + " " + event.getSurname();
        var content = """
                <div class="notification-content">
                               <h4>Welcome to TeachMe, %s!</h4>
                               <p>We're thrilled to have you join our learning community. TeachMe is dedicated to helping you reach your goals and unlock your full potential.</p>
                               <p>Here’s how to get started:</p>
                               <ul>
                                   <li>Access your <strong>Dashboard</strong> to view your courses and progress.</li>
                                   <li>Explore our <strong>Course Library</strong> to find exciting new topics to learn.</li>
                                   <li>Set up your learning goals and preferences to personalize your experience.</li>
                               </ul>
                               <p>If you ever need help, visit our <a href="/student/help">Help Center</a>.</p>
                           </div>
                """.formatted(fullName);

        String previewText = String.format("Welcome to TeachMe, %s! We're thrilled to have you join our learning community. TeachMe is dedicated to helping you reach your goals and unlock your full potential.", fullName);

        return new NotificationTemplate(content, previewText);
    }

    @Data
    @AllArgsConstructor
    public static class NotificationTemplate {
        public String content;
        public String previewText;
    }
}
