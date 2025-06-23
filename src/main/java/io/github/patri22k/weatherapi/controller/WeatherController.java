package io.github.patri22k.weatherapi.controller;

import io.github.patri22k.weatherapi.enums.ResourceSource;
import io.github.patri22k.weatherapi.exception.WeatherAPIRequestException;
import io.github.patri22k.weatherapi.model.RawWeatherModel;
import io.github.patri22k.weatherapi.model.WeatherModel;
import io.github.patri22k.weatherapi.model.WeatherWrapper;
import io.github.patri22k.weatherapi.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {
    private final WeatherService weatherService;
    private final RateLimiterService rateLimiterService;
    private final ErrorMapperService errorMapperService;

    @Autowired
    public WeatherController(
            WeatherService weatherService,
            RateLimiterService rateLimiterService, ErrorMapperService errorMapperService) {
        this.weatherService = weatherService;
        this.rateLimiterService = rateLimiterService;
        this.errorMapperService = errorMapperService;
    }

    @GetMapping("/weather")
    public ResponseEntity<?> getWeather(@RequestParam String location, HttpServletRequest request) {
        String clientIp = request.getRemoteAddr();
        if (!rateLimiterService.isAllowed(clientIp)) {
            return ResponseEntity
                    .status(HttpStatus.TOO_MANY_REQUESTS)
                    .header("Retry-After", "60")
                    .body("Rate limit exceeded. Please try again later.");
        }

        try {
            Pair<RawWeatherModel, ResourceSource> data = weatherService.getWeather(location);

            WeatherModel dto = WeatherModel.createWeatherModel(data.getFirst());
            WeatherWrapper body = new WeatherWrapper(data.getSecond().toString(), dto);

            return ResponseEntity.ok(body);
        } catch (WeatherAPIRequestException e) {
            return errorMapperService.toErrorModel(e).toResponseEntity();
        }
    }

}
