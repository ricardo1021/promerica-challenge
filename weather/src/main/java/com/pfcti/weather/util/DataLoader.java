package com.pfcti.weather.util;

import com.pfcti.weather.model.WeatherHistory;
import com.pfcti.weather.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final WeatherRepository weatherRepository;

    @Override
    public void run(String... args) throws Exception {
        WeatherHistory weatherHistory = new WeatherHistory();
        weatherHistory.setWeather("Zocca");
        weatherHistory.setLat(10.01);
        weatherHistory.setLon(-84.10);
        weatherHistory.setCreated(new Date());
        weatherHistory.setTempMin(278.56);
        weatherHistory.setTempMax(284.96);
        weatherHistory.setHumidity(84.0);


        weatherRepository.save(weatherHistory);
    }
}
