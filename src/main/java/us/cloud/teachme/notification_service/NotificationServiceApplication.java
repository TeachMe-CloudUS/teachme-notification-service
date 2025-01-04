package us.cloud.teachme.notification_service;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@OpenAPIDefinition(
		info = @Info(
				title = "Teachme Notification Service API",
				version = "1.0",
				description = "API for managing and sending in-app and mail notifications for the teachme platform",
				contact = @Contact(name = "API Support", email = "ramnak@alum.us.es")
		),
		servers = @Server(url = "${GATEWAY_SERVER_URL:http://localhost:8888}")
)
@SpringBootApplication
@EnableWebSocketMessageBroker
@EnableMongoRepositories
public class NotificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceApplication.class, args);
	}

}
