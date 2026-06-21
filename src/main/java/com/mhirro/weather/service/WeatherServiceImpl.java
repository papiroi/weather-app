package com.mhirro.weather.service;

import com.mhirro.weather.dto.WeatherDto;
import com.mhirro.weather.entity.Weather;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    @Caching(cacheable = {@Cacheable("primary")}, put = {@CachePut("backup")})
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

    @Override
    @CircuitBreaker(name = "secondary")
    @Retry(name = "secondary",  fallbackMethod = "getWeatherFromCache")
    @Caching(cacheable = {@Cacheable("secondary")}, put = {@CachePut("backup")})
    public WeatherDto getWeatherFromSecondary(String city, RuntimeException e) {
        log.debug("Encountered exception at primary API: {}", e.getClass());
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

//    @Cacheable("backup")
//    public WeatherDto getWeatherFromCache(String city, RuntimeException re, Throwable e) {
//        log.debug("Encountered exception at secondary API: {}", re.getClass());
//        log.info("Fetching from cache for city {}", city);
//
//        throw re;
//    }
}
