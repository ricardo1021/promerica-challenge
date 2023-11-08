package com.pfcti.weather;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pfcti.weather.dto.WeatherRequest;
import com.pfcti.weather.service.IWeatherService;
import com.pfcti.weather.service.impl.WeatherServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WeatherApplicationTests {

	private IWeatherService weatherService = new WeatherServiceImpl();

	@Test
	void contextLoads() {
	}

	@Test
	public void getData() throws JsonProcessingException {

		WeatherRequest weatherRequest = new WeatherRequest();
		weatherRequest.setLat(10.01);
		weatherRequest.setLon(-80.1);
		weatherService.getWeatherData(weatherRequest);
	}

}
