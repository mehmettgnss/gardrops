package com.gardrops.imageupload.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class RestTemplateConfig {

    @Value("${app.internal.api.key}")
    private String internalApiKey;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(
            (request, body, execution) -> {
                request.getHeaders().set("X-Internal-Api-Key", internalApiKey);
                return execution.execute(request, body);
            }
        ));
        return restTemplate;
    }
} 