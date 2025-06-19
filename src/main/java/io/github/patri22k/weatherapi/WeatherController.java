package io.github.patri22k.weatherapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

    @GetMapping("/weather")
    protected String getWeather() {
        return "The weather is chill and sunny without clouds!";
    }
}
