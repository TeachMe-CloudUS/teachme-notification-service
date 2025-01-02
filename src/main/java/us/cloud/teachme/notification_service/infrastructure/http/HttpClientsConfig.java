package us.cloud.teachme.notification_service.infrastructure.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class HttpClientsConfig {

    @Value("${teachme-student-service.url}")
    private String studentServiceBaseUrl;

    @Value("${teachme-email-azure-function.url}")
    private String azureFunctionBaseUrl;

    @Bean
    public RestClient studentServiceClient() {
        return RestClient.builder().baseUrl(studentServiceBaseUrl).build();
    }

    @Bean
    public RestClient azureFunctionClient() {
        return RestClient.builder().baseUrl(azureFunctionBaseUrl).build();
    }
}
