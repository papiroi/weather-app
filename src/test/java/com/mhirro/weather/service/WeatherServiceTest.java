package com.mhirro.weather.service;

import com.mhirro.weather.configuration.ConnectionConfiguration;
import com.mhirro.weather.dto.WeatherDto;
import com.mhirro.weather.entity.Current;
import com.mhirro.weather.entity.Main;
import com.mhirro.weather.entity.Weather;
import com.mhirro.weather.entity.Wind;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@ExtendWith(MockitoExtension.class)
@RestClientTest(WeatherService.class)
public class WeatherServiceTest {

    @InjectMocks
    @Autowired
    private WeatherService service;

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private ObjectMapper mapper;

    private static Weather weather1;
    private static Weather weather2;

    @BeforeAll
    public static void init() {
        weather1 = Weather.builder()
                .current(Current.builder()
                        .temperature("69")
                        .windSpeed("420")
                        .build())
                .build();

        weather2 = Weather.builder()
                .main(Main.builder()
                        .temperature("67")
                        .build())
                .wind(Wind.builder()
                        .speed("42069")
                        .build())
                .build();
    }

    @Test
    public void testWeatherServiceReturnsValue() {

        String weatherString = mapper.writeValueAsString(weather1);

        this.server.expect(requestTo("http://localhost:8081/weather/1"))
                .andRespond(withSuccess(weatherString, MediaType.APPLICATION_JSON));

        WeatherDto weather = service.getWeather("Manila");
        assertNotNull(weather);
    }

    @Test
    public void testWeatherServicePrimaryApiFails() {

        String weatherString = mapper.writeValueAsString(weather2);

        this.server.expect(requestTo("http://localhost:8081/weather/2"))
                .andRespond(withSuccess(weatherString, MediaType.APPLICATION_JSON));

        WeatherDto weather = service.getWeatherFromSecondary("Manila", new RuntimeException());
        assertNotNull(weather);
    }

}
