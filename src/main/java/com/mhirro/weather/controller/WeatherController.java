package com.mhirro.weather.controller;

import com.mhirro.weather.dto.WeatherDto;
import com.mhirro.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class WeatherController {

    @Autowired
    private WeatherService service;

    @GetMapping(path = "/weather")
    public WeatherDto weather(@RequestParam(name = "city", required = false,
                    defaultValue = "${weather.default.city}") String city) {
        return service.getWeather(city);
    }
}
