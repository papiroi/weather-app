package com.mhirro.weather.service;

import com.mhirro.weather.dto.WeatherDto;
import com.mhirro.weather.entity.Weather;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
public class WeatherServiceImpl implements WeatherService{

    @Autowired
    @Qualifier("primaryClient")
    private RestClient primaryClient;

    @Autowired
    @Qualifier("secondaryClient")
    private RestClient secondaryClient;

    @Override
    @Cacheable
    @CircuitBreaker(name = "primary", fallbackMethod = "getWeatherFromSecondary")
    @Retry(name = "primary")
    public WeatherDto getWeather(String city) {
        log.info("Calling primary weather API for city {}", city);
        Weather response = primaryClient.get()
                .retrieve()
                .body(Weather.class);

        return WeatherDto.builder()
                .tempDegrees(response.getCurrent().getTemperature())
                .windSpeed(response.getCurrent().getWindSpeed())
                .build();
    }

    @Retry(name = "secondary")
    @Cacheable
    public WeatherDto getWeatherFromSecondary(String city, RuntimeException e) {
        log.info("Calling secondary weather API for city {}", city);
        Weather response = secondaryClient.get()
                .retrieve()
                .body(Weather.class);

        return WeatherDto.builder()
                .tempDegrees(response.getMain().getTemperature())
                .windSpeed(response.getWind().getSpeed())
                .build();
    }
}
