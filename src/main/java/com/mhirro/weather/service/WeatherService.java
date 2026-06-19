package com.mhirro.weather.service;

import com.mhirro.weather.dto.WeatherDto;

public interface WeatherService {
    WeatherDto getWeather(String city);
}
