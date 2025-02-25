package weather.model;

import jakarta.persistence.*;


@Entity
@Table(name = "weather")
public class WeatherData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

}
