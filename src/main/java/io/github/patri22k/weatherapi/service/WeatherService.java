package io.github.patri22k.weatherapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.patri22k.weatherapi.enums.ResourceSource;
import io.github.patri22k.weatherapi.exception.WeatherAPIRequestException;
import io.github.patri22k.weatherapi.model.RawWeatherModel;
import io.github.patri22k.weatherapi.config.properties.WeatherConfigProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Service
public class WeatherService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final WeatherConfigProperties weatherConfigProperties;

    public WeatherService(
            StringRedisTemplate redisTemplate,
            ObjectMapper objectMapper, WeatherConfigProperties weatherConfigProperties) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.weatherConfigProperties = weatherConfigProperties;
    }

    public Pair<RawWeatherModel, ResourceSource> getWeather(String location) throws WeatherAPIRequestException {
        // Cache
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String cachedWeather = ops.get(location);

        if (cachedWeather != null) {
            try {
                return Pair.of(parseWeatherModel(cachedWeather), ResourceSource.CACHE);
            } catch (JsonProcessingException ignored) {
                // We ignore this because we refresh the cache if there is invalid data
            }
        }

        String weatherUrlWithApiKey = String.format(
                "%s/%s?key=%s", weatherConfigProperties.getUrl(), location, weatherConfigProperties.getApiKey());
        try {
            String response = restTemplate.getForObject(weatherUrlWithApiKey, String.class);
            if (response == null) {
                throw new WeatherAPIRequestException("API returned null body", null);
            }

            ops.set(location, response, Duration.ofMinutes(10));

            // Return Response from 3rd Party
            try {
                return Pair.of(parseWeatherModel(response), ResourceSource.API);
            } catch (JsonProcessingException e) {
                throw new WeatherAPIRequestException("API returned invalid data", e);
            }
        } catch (HttpClientErrorException e) {
            throw new WeatherAPIRequestException(e);
        }
    }

    private RawWeatherModel parseWeatherModel(String data) throws JsonProcessingException {
        return objectMapper.readValue(data, RawWeatherModel.class);
    }
}
