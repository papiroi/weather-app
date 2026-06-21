package com.mhirro.weather.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ConnectionConfiguration {

    @Autowired
    private RestClient.Builder builder;

    @Bean(name = "primaryClient")
    public RestClient primaryClient() {
        return builder
                .baseUrl("http://localhost:8081/weather/1")
                .build();
    }

    @Bean(name = "secondaryClient")
    public RestClient secondaryClient() {
        return builder
                .baseUrl("http://localhost:8081/weather/2")
                .build();
    }
}
