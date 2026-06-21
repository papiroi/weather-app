package com.mhirro.weather.service;

import com.mhirro.weather.dto.WeatherDto;
import com.mhirro.weather.entity.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class WeatherServiceImpl implements WeatherService{

    //@Value("${weather.primary.url}")
    private static final String PRIMARY_URL = "http://localhost:8081/weather";
//    public WeatherServiceImpl(@Value("${weather.primary.url") String primaryUrl) {
//        PRIMARY_URL = primaryUrl;
//    }

    @Autowired
    private RestClient.Builder builder;

    @Override
    public WeatherDto getWeather(String city) {
        RestClient client = builder.build();

        Weather response = client.get()
                .uri(PRIMARY_URL)
                .retrieve()
                .body(Weather.class);

        return WeatherDto.builder()
                .tempDegrees(response.getCurrent().getTemperature())
                .windSpeed(response.getCurrent().getWindSpeed())
                .build();
    }
}
