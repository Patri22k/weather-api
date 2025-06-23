package io.github.patri22k.weatherapi.config.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "weather")
public class WeatherConfigProperties {
    private final String url;
    private final String apiKey;

}
