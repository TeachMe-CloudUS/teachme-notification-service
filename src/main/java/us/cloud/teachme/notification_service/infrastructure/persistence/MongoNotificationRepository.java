package us.cloud.teachme.notification_service.infrastructure.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import us.cloud.teachme.notification_service.domain.Notification;

import java.util.List;

@Service
public interface MongoNotificationRepository extends MongoRepository<Notification, String> {

    List<Notification> findAllByUserId(String userId);
}