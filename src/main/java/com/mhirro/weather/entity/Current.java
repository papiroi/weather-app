package com.mhirro.weather.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Current {
    @JsonProperty("temperature")
    private String temperature;
    @JsonProperty("wind_speed")
    private String windSpeed;
}
