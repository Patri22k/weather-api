package io.github.patri22k.weatherapi.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.patri22k.weatherapi.Model.RawWeatherModel;
import io.github.patri22k.weatherapi.Model.WeatherModel;
import io.github.patri22k.weatherapi.Model.WeatherWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@RestController
public class WeatherController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public WeatherController(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${base.weather.url}")
    private String baseWeatherUrl;

    @GetMapping("/weather")
    public ResponseEntity<?> getWeather(@RequestParam String location) {

        // 1. Check Cache
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String cachedWeather = ops.get(location);

        // 2. Check Cache Response
        if (cachedWeather != null) {
            return getWeatherModel(cachedWeather);
        }

        // 3. Request Weather API and
        // 4. Weather API Response
        String weatherUrlWithApiKey = String.format("%s/%s?key=%s", baseWeatherUrl, location, apiKey);
        String response = restTemplate.getForObject(weatherUrlWithApiKey, String.class);

        // 5. Saved Cached Results
        if (response != null) {
            ops.set(location, response, Duration.ofSeconds(10));

            // 6. Return Response from 3rd Party
            return getWeatherModel(response);
        }

        // Default response
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<?> getWeatherModel(String response) {
        try {
            RawWeatherModel rawWeatherModel = objectMapper.readValue(response, RawWeatherModel.class);
            WeatherModel weatherModel = WeatherModel.createWeatherModel(rawWeatherModel);
            return ResponseEntity
                    .ok()
                    .body(new WeatherWrapper("CACHE", weatherModel));
        } catch (JsonProcessingException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error parsing cached weather data.");
        }
    }
}
