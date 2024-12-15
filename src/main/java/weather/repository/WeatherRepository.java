package weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import weather.model.WeatherData;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherData, Long> {

}
