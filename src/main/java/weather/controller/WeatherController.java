package weather.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/weather")
public class WeatherController {


    @GetMapping(path = "/weather/id", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
        //200
    String saveNewWeather2() {
        return "SomeString";
    }

    @PostMapping(path = "", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    String saveNewWeather() {
        return "SomeString";
    }

    @DeleteMapping(path = "/weather/<id>", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
        //200
    String saveNewWeather3() {
        return "SomeString";
    }

    // Optional Parameters

    // one

    @GetMapping(path = "", params = {"date"})
    String optionalDate() {
        System.out.println("date");
        return "date";
    }

    @GetMapping(path = "", params = {"sort"})
    String optionalSort() {
        System.out.println("sort");
        return "sort";
    }

    @GetMapping(path = "", params = {"city"})
    String optionalCity() {
        System.out.println("city");
        return "city";
    }


    // two

    @GetMapping(path = "", params = {"date", "city"})
    String optionalDateAndCity() {
        System.out.println("date" + "city");
        return "date" + "city";
    }

    @GetMapping(path = "", params = {"date", "sort"})
    String optionalDateAndSort() {
        System.out.println("date" + "sort");
        return "date" + "sort";
    }

    @GetMapping(path = "", params = {"sort", "city"})
    String optionalSortAndCity() {
        System.out.println("sort" + "city");
        return "sort" + "city";
    }


    // three, all

    @GetMapping(path = "", params = {"date", "sort", "city"})
    String optionalSortAndCityAndDate() {
        System.out.println("date" + "sort" + "city");
        return "date" + "sort" + "city";
    }


}