package io.github.patri22k.weatherapi.config;

import io.github.patri22k.weatherapi.config.properties.WeatherConfigProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(WeatherConfigProperties.class)
@Configuration
public class WeatherConfig {
}
