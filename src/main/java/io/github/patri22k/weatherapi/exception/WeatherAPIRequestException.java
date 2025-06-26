package io.github.patri22k.weatherapi.exception;

public class WeatherAPIRequestException extends RuntimeException {
    public WeatherAPIRequestException(String message) {
        super(message);
    }

    public WeatherAPIRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
