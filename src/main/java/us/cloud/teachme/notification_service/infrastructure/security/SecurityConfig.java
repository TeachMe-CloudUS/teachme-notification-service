package us.cloud.teachme.notification_service.infrastructure.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import us.cloud.teachme.authutils.config.AuthSecurityConfiguration;

@Configuration
@Import(AuthSecurityConfiguration.class)
public class SecurityConfig {

}
