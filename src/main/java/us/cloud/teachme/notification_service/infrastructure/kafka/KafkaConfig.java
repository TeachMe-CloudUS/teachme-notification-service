package us.cloud.teachme.notification_service.infrastructure.kafka;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.ConsumerFactory;

@Configuration
@Import(us.cloud.teachme.kafkaconfig.config.KafkaConfig.class)
public class KafkaConfig {

    private final ConsumerFactory<String, Object> consumerFactory;

    public KafkaConfig(ConsumerFactory<String, Object> consumerFactory) {
        this.consumerFactory = consumerFactory;
    }

}
