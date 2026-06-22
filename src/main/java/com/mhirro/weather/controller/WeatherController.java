package com.mhirro.weather.controller;

import com.mhirro.weather.dto.WeatherDto;
import com.mhirro.weather.service.WeatherService;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1")
public class WeatherController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherController.class);

    @Autowired
    private WeatherService service;

    @GetMapping(path = "/weather")
    public WeatherDto weather(@RequestParam(name = "city", required = false,
                    defaultValue = "${weather.default.city}") String city) {
        LOGGER.info("Requested");

        return service.getWeather(city);
    }
}
