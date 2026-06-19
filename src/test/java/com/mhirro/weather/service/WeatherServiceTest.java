package com.mhirro.weather.service;

import com.mhirro.weather.dto.WeatherDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class WeatherServiceTest {
    @Autowired
    private WeatherService service;

    @Test
    public void testWeatherServiceReturnsValue() {
        WeatherDto dto = service.getWeather("Melbourne");
        assertNotNull(dto);
    }

}
