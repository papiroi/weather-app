package com.mhirro.weather.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeatherDto {
    private String windSpeed;
    private String tempDegrees;
}
