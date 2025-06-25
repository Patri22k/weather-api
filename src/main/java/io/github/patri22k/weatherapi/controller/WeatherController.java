package io.github.patri22k.weatherapi.controller;

import io.github.patri22k.weatherapi.service.RateLimiterService;
import io.github.patri22k.weatherapi.service.WeatherService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

    private final WeatherService weatherService;
    private final RateLimiterService rateLimiterService;

    @Autowired
    public WeatherController(WeatherService weatherService, RateLimiterService rateLimiterService) {
        this.weatherService = weatherService;
        this.rateLimiterService = rateLimiterService;
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

        return weatherService.getWeather(location);
    }

}
