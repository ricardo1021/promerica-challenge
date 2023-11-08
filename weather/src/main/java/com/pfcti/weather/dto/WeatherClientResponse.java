package com.pfcti.weather.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeatherClientResponse {

    @JsonInclude(JsonInclude.Include. NON_NULL)
    private String lat;
    @JsonInclude(JsonInclude.Include. NON_NULL)
    private String lon;
    @JsonInclude(JsonInclude.Include. NON_NULL)
    private String weather;
    @JsonInclude(JsonInclude.Include. NON_NULL)
    private String tempMin;
    @JsonInclude(JsonInclude.Include. NON_NULL)
    private String tempMax;
    @JsonInclude(JsonInclude.Include. NON_NULL)
    private String humidity;


}
