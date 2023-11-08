package com.pfcti.weather.repository;

import com.pfcti.weather.model.WeatherHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherHistory, Long> {

    @Query(value = "SELECT * FROM weather_history w WHERE w.lat = :lat and w.lon = :lon", nativeQuery = true)
    WeatherHistory findByCoord(@Param("lat") double lat, @Param("lon") double lon);
}
