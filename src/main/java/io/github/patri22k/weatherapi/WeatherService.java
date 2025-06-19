package io.github.patri22k.weatherapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class WeatherService {

    private final StringRedisTemplate redisTemplate;

    @Autowired
    public WeatherService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getWeather(String location) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String cacheKey = "weather:" + location;

        String cachedWeather = ops.get(cacheKey);
        if (cachedWeather != null) {
            return "[CACHE] " + cachedWeather;
        }

        String apiWeather = hardcodedWeatherApi(location);
        ops.set(cacheKey, apiWeather, Duration.ofMinutes(10));

        return "[API] " + apiWeather;
    }

    private String hardcodedWeatherApi(String location) {
        return "Sunny in " + location + ", 25°C";
    }
}
