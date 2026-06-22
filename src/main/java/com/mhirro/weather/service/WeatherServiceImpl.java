package com.mhirro.weather.service;

import com.mhirro.weather.dto.WeatherDto;
import com.mhirro.weather.entity.Weather;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
public class WeatherServiceImpl implements WeatherService{

    @Autowired
    private RestClient.Builder builder;

    @Value("${weather.primary.apikey}")
    private String primaryApiKey;

    @Value("${weather.primary.url}")
    private String primaryUrl;

    @Value("${weather.fallback.apikey}")
    private String secondaryApiKey;

    @Value("${weather.fallback.url}")
    private String secondaryUrl;

    @Override
    @Cacheable(cacheNames = {"primary"})
    @Retry(name = "primary", fallbackMethod = "getWeatherFromSecondary")
    public WeatherDto getWeather(String city) {
        log.info("Calling primary weather API for city {}", city);

        RestClient client = builder
                .baseUrl(primaryUrl)
                .build();

        Weather response = client.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("access_key", primaryApiKey)
                        .queryParam("query", city)
                        .build())
                .retrieve()
                .body(Weather.class);

        if (response != null) {
            return WeatherDto.builder()
                    .tempDegrees(response.getCurrent().getTemperature())
                    .windSpeed(response.getCurrent().getWindSpeed())
                    .build();
        } else {
            return null;
        }
    }

    @Override
    @Retry(name = "secondary")
    @Cacheable(cacheNames = {"secondary"})
    public WeatherDto getWeatherFromSecondary(String city, RuntimeException e) {
        log.debug("Encountered exception at primary API: {}", e.getClass());
        log.info("Calling secondary weather API for city {}", city);

        RestClient client = builder
                .baseUrl(secondaryUrl)
                .build();

        Weather response = client.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("q", city)
                        .queryParam("appid", secondaryApiKey)
                        .build())
                .retrieve()
                .body(Weather.class);

        if (response != null) {
            return WeatherDto.builder()
                    .tempDegrees(response.getMain().getTemperature())
                    .windSpeed(response.getWind().getSpeed())
                    .build();
        } else {
            return null;
        }
    }
}
