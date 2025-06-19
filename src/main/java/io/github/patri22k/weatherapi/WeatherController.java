package io.github.patri22k.weatherapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

    private final WeatherService service;

    protected WeatherController(WeatherService service) {
        this.service = service;
    }

    @GetMapping("/weather")
    protected String getWeather(@RequestParam String location) {
        return service.getWeather(location);
    }
}
