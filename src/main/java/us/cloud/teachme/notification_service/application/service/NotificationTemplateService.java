package us.cloud.teachme.notification_service.application.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.cloud.teachme.notification_service.presentation.event.StudentCreatedEvent;

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

        var emailContent = """
                <div class="notification-content" style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                    <h4 style="color: #2a7ae2;">Welcome to TeachMe, %s!</h4>
                    <p>We're thrilled to have you join our learning community. TeachMe is dedicated to helping you reach your goals and unlock your full potential.</p>
                    <p>Here’s how to get started:</p>
                    <ul>
                        <li>Access your <strong>Dashboard</strong> to view your courses and progress.</li>
                        <li>Explore our <strong>Course Library</strong> to find exciting new topics to learn.</li>
                        <li>Set up your learning goals and preferences to personalize your experience.</li>
                    </ul>
                    <p>Visit our website to get started: <a href="https://www.teachme.com" style="color: #2a7ae2; text-decoration: none;">TeachMe.com</a></p>
                    <p>If you ever need assistance, our <a href="https://www.teachme.com/help" style="color: #2a7ae2; text-decoration: none;">Help Center</a> is just a click away.</p>
                    <p>Welcome aboard, and happy learning!</p>
                </div>
                """.formatted(fullName);

        String previewText = String.format("Welcome to TeachMe, %s! We're thrilled to have you join our learning community. TeachMe is dedicated to helping you reach your goals and unlock your full potential.", fullName);

        return new NotificationTemplate(content, previewText, emailContent);
    }

    @Data
    @AllArgsConstructor
    public static class NotificationTemplate {
        public String content;
        public String previewText;
        public String emailContent;
    }
}
