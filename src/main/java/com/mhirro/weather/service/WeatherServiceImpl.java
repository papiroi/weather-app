package com.mhirro.weather.service;

import com.mhirro.weather.dto.WeatherDto;
import com.mhirro.weather.entity.Weather;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
public class WeatherServiceImpl implements WeatherService{

    @Autowired
    private RestClient.Builder builder;

    @Override
    @Cacheable("primary")
    @CircuitBreaker(name = "primary")
    @Retry(name = "primary", fallbackMethod = "getWeatherFromSecondary")
    public WeatherDto getWeather(String city) {
        log.info("Calling primary weather API for city {}", city);

        RestClient client = builder.build();

        Weather response = client.get()
                .uri("http://localhost:8081/weather/1")
                .retrieve()
                .body(Weather.class);

        return WeatherDto.builder()
                .tempDegrees(response.getCurrent().getTemperature())
                .windSpeed(response.getCurrent().getWindSpeed())
                .build();
    }

    @CircuitBreaker(name = "secondary")
    @Retry(name = "secondary")
    @Cacheable("secondary")
    public WeatherDto getWeatherFromSecondary(String city, RuntimeException e) {
        log.info("Calling secondary weather API for city {}", city);

        RestClient client = builder.build();

        Weather response = client.get()
                .uri("http://localhost:8081/weather/2")
                .retrieve()
                .body(Weather.class);

        return WeatherDto.builder()
                .tempDegrees(response.getMain().getTemperature())
                .windSpeed(response.getWind().getSpeed())
                .build();
    }
}
