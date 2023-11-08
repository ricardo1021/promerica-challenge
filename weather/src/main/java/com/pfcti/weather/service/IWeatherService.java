package com.pfcti.weather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pfcti.weather.dto.ServiceResponse;
import com.pfcti.weather.dto.WeatherRequest;

import java.util.List;

public interface IWeatherService {
    List<ServiceResponse> getWeather();

    ServiceResponse getWeatherData(WeatherRequest weatherRequest) throws JsonProcessingException;

    List<ServiceResponse> getWeatherHistoryData();
}
