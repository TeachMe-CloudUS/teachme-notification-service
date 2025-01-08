package us.cloud.teachme.notification_service.infrastructure.messaging;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(us.cloud.teachme.kafkaconfig.config.KafkaConfig.class)
public class KafkaConfig {

}
