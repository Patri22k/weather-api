package io.github.patri22k.weatherapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.patri22k.weatherapi.exception.WeatherAPIRequestException;
import io.github.patri22k.weatherapi.model.RawWeatherModel;
import io.github.patri22k.weatherapi.model.WeatherModel;
import io.github.patri22k.weatherapi.model.WeatherWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Service
public class WeatherService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public WeatherService(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${base.weather.url}")
    private String baseWeatherUrl;

    public ResponseEntity<?> getWeather(@RequestParam String location) {

        // 1. Check Cache
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String cachedWeather = ops.get(location);

        // 2. Check Cache Response
        if (cachedWeather != null) {
            return getWeatherModel("CACHE", cachedWeather);
        }

        // 3. Request Weather API and
        // 4. Weather API Response
        String weatherUrlWithApiKey = String.format("%s/%s?key=%s", baseWeatherUrl, location, apiKey);
        String response = restTemplate.getForObject(weatherUrlWithApiKey, String.class);

        // 5. Saved Cached Results
        if (response != null) {
            ops.set(location, response, Duration.ofMinutes(10));

            // 6. Return Response from 3rd Party
            return getWeatherModel("API", response);
        }

        return ResponseEntity.internalServerError().build();
    }

    private ResponseEntity<?> getWeatherModel(String resource, String response) {
        try {
            RawWeatherModel rawWeatherModel = objectMapper.readValue(response, RawWeatherModel.class);
            WeatherModel weatherModel = WeatherModel.createWeatherModel(rawWeatherModel);
            return ResponseEntity
                    .ok()
                    .body(new WeatherWrapper(resource, weatherModel));
        } catch (JsonProcessingException e) {
            throw new WeatherAPIRequestException("Error parsing weather data from " + resource, e);
        }
    }

}
