package com.pfcti.weather.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pfcti.weather.dto.ServiceResponse;
import com.pfcti.weather.dto.WeatherRequest;
import com.pfcti.weather.service.IWeatherService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/api/v1/weather")
public class WeatherController {

    @Autowired
    IWeatherService weatherService;

    @CircuitBreaker(name = "weather", fallbackMethod = "fallbackMethod")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceResponse> getWeather(@RequestBody WeatherRequest weatherRequest) throws JsonProcessingException {
            return new ResponseEntity<>(weatherService.getWeatherData(weatherRequest), HttpStatus.OK);
    }


    @GetMapping(value= "/history",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<ServiceResponse> getWeatherHistoryData() {
        return weatherService.getWeatherHistoryData();
    }

    public String fallbackMethod(ResponseEntity weatherRequest, RuntimeException runtimeException) {
//        log.info("Executing Fallback method");
        return "Error consuming weather client api!";
    }

}
