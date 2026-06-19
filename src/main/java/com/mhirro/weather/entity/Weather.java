package com.mhirro.weather.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Weather {
    @JsonProperty("current")
    private Current current;
    @JsonProperty("main")
    private Main main;
    @JsonProperty("wind")
    private Wind wind;
}
