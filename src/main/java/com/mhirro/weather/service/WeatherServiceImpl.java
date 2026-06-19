package com.mhirro.weather.service;

import com.mhirro.weather.dto.WeatherDto;
import com.mhirro.weather.entity.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class WeatherServiceImpl implements WeatherService{

    //@Value("${weather.primary.url}")
    private static final String PRIMARY_URL = "http://api.openweathermap.org/data/2.5/weather?q=melbourne,AU&appid=2326504fb9b100bee21400190e4dbe6d&units=metric";
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
                .tempDegrees(response.getMain().getTemperature())
                .windSpeed(response.getWind().getSpeed())
                .build();
    }
}
