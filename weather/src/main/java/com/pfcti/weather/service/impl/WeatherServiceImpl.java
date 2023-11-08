package com.pfcti.weather.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pfcti.weather.dto.ServiceResponse;
import com.pfcti.weather.dto.WeatherClientResponse;
import com.pfcti.weather.dto.WeatherRequest;
import com.pfcti.weather.model.WeatherHistory;
import com.pfcti.weather.repository.WeatherRepository;
import com.pfcti.weather.service.IWeatherService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Transactional
public class WeatherServiceImpl implements IWeatherService {

    public static final int MINUTES_RANGE = 1;
    @Autowired
    WeatherRepository weatherRepository;

    @Autowired
    WebClient webClient;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public List<ServiceResponse> getWeather() {
        List<WeatherHistory> weatherHistoryList = weatherRepository.findAll();
        return weatherHistoryList.stream().map(weatherHistory -> modelMapper.map(weatherHistory, ServiceResponse.class))
                .collect(Collectors.toList());
    }



    @Override
    public ServiceResponse getWeatherData(WeatherRequest weatherRequest) throws JsonProcessingException {
        ServiceResponse serviceResponse ;
        WeatherHistory weatherHistory = findWeatherByCoord(weatherRequest.getLat(), weatherRequest.getLon());
        if(!Objects.isNull(weatherHistory)){
            long diffMinutes = getDiffMinutes(weatherHistory);
            if (evaluateMinutes(diffMinutes)){
                weatherHistory = saveUpdateWeatherHistory(weatherRequest, weatherHistory);
            }
        }else {
            weatherHistory = saveUpdateWeatherHistory(weatherRequest, weatherHistory);
        }
        serviceResponse = modelMapper.map(weatherHistory, ServiceResponse.class);
        return serviceResponse;
    }

    @Override
    public List<ServiceResponse> getWeatherHistoryData() {
        List<WeatherHistory> weatherHistoryList = weatherRepository.findAll();

        return weatherHistoryList.stream().map(weatherHistory -> modelMapper.map(weatherHistory, ServiceResponse.class))
                .collect(Collectors.toList());
    }

    private boolean evaluateMinutes(long diffMinutes) {
        return diffMinutes > MINUTES_RANGE;
    }

    private long getDiffMinutes(WeatherHistory weatherHistory) {
        long now = new Date().getTime();
        long duration = now - weatherHistory.getCreated().getTime();
        long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        return diffMinutes;
    }

    private WeatherHistory saveUpdateWeatherHistory(WeatherRequest weatherRequest, WeatherHistory weatherHistory) throws JsonProcessingException {
        WeatherClientResponse weatherClientResponse;
        weatherClientResponse = getDataWeatherApi(weatherRequest.getLat(), weatherRequest.getLon());
        weatherHistory = modelMapper.map(weatherClientResponse, WeatherHistory.class);
        weatherHistory.setCreated(new Date());
        weatherRepository.saveAndFlush(weatherHistory);
        return weatherHistory;
    }

    private WeatherHistory findWeatherByCoord(double lat, double lon) {
        return weatherRepository.findByCoord(lat, lon);

    }

    private WeatherClientResponse getDataWeatherApi(double lat, double lon) throws JsonProcessingException {

        String urlApi = "https://api.openweathermap.org/data/2.5/weather?";
        String apiKey= "b50bf36594cc8bb09cc5a79373483656";
        StringBuilder uriWeatherApi = new StringBuilder();
        uriWeatherApi.append(urlApi).append("lat=").append(lat)
                .append("&lon=").append(lon+"&appid=")
                .append(apiKey);

        String weatherJson = webClient.get().uri(uriWeatherApi.toString())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return transforDataWeatherMap(weatherJson);
    }

    private WeatherClientResponse transforDataWeatherMap(String weatherJson) throws JsonProcessingException {

        JsonNode node = objectMapper.readTree(weatherJson);
            WeatherClientResponse weatherClientResponse = new WeatherClientResponse();

        weatherClientResponse.setLat(node.findValue("lat").toString());
        weatherClientResponse.setLon(node.findValue("lon").toString());
        weatherClientResponse.setTempMin(node.findValue("temp_min").toString());
        weatherClientResponse.setTempMax(node.findValue("temp_max").toString());
        weatherClientResponse.setHumidity(node.findValue("humidity").toString());
        weatherClientResponse.setWeather(node.findValue("main").toString());

        return weatherClientResponse;

    }
}
