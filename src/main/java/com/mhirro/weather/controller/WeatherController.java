package com.mhirro.weather.controller;

import com.mhirro.weather.dto.WeatherDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class WeatherController {

    @GetMapping(path = "/weather")
    public WeatherDto weather(@RequestParam(name = "city", required = false,
                    defaultValue = "${weather.default.city}") String city) {
        return WeatherDto.builder()
                .tempDegrees("100")
                .windSpeed("120")
                .build();
    }
}
