package org.knou.keyproject.domain.chatgpt.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

// 2023.8.1(화) 22h35
@Configuration
@PropertySource(value = "classpath:application-local.yml")
public class OpenAIRestTemplateConfig {

    @Value("${openai.api.key}")
    private String openaiApiKey;

    @Bean
    @Qualifier("openaiRestTemplate")
    public RestTemplate openaiRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + openaiApiKey);
            return execution.execute(request, body);
        });

        return restTemplate;
    }
}
