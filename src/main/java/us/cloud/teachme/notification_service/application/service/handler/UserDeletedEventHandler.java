package us.cloud.teachme.notification_service.application.service.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import us.cloud.teachme.notification_service.application.dto.EmailNotificationContent;
import us.cloud.teachme.notification_service.application.dto.NotificationContent;
import us.cloud.teachme.notification_service.application.ports.EmailNotifier;
import us.cloud.teachme.notification_service.infrastructure.messaging.events.UserDeletedEvent;
import us.cloud.teachme.notification_service.infrastructure.persistence.MongoNotificationRepository;

@Service
@RequiredArgsConstructor
public class UserDeletedEventHandler implements KafkaEventHandler {

    private static final String TOPIC = "auth-service.user.deleted";

    private final MongoNotificationRepository repository;
    private final EmailNotifier emailNotifier;
    private final ObjectMapper mapper;

    @Override
    public boolean canHandle(String topic) {
        return TOPIC.equals(topic);
    }

    @Override
    public void handle(String message) {
        try {
            var event = mapper.readValue(message, UserDeletedEvent.class);

            repository.deleteAll(repository.findAllByUserId(event.getId()));

            var content = NotificationContent.builder()
                    .userId(event.getId())
                    .type("USER_DELETED")
                    .title("Your account has been deleted!")
                    .message("Your account has been deleted.")
                    .previewText("Your account has been deleted.")
                    .build();

            var mailContent = EmailNotificationContent.create(content,
                    """
                            <div class="notification-content" style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                                <h4 style="color: #e02a2a;">Your TeachMe Account Has Been Deleted</h4>
                                <p>Dear User,</p>
                                <p>We’re writing to let you know that your TeachMe account has been successfully deleted. We're sorry to see you go, but we respect your decision.</p>
                                <p>While your account has been removed, here are some things to keep in mind:</p>
                                <ul>
                                    <li>Your personal data has been securely removed from our system.</li>
                                    <li>Any progress in courses or settings associated with your account has been deleted and cannot be recovered.</li>
                                </ul>
                                <p>Thank you for being a part of TeachMe. We wish you all the best in your learning journey!</p>
                                <p>Regards,</p>
                                <p>The TeachMe Team</p>
                            </div>
                            """);

            emailNotifier.notify(mailContent, event.getEmail());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
