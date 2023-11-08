package com.pfcti.weather.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name="weather_history")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeatherHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double lat;
    private Double lon;
    private String weather;
    private Double tempMin;
    private Double tempMax;
    private Double humidity;
    private Date created;

}
