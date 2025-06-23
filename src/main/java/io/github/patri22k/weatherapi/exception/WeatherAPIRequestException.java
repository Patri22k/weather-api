package io.github.patri22k.weatherapi.exception;

import io.github.patri22k.weatherapi.model.ErrorModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class WeatherAPIRequestException extends RuntimeException {

    public WeatherAPIRequestException(HttpClientErrorException cause) {
        this(cause.getResponseBodyAsString(), cause);
    }

    public WeatherAPIRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public ErrorModel createErrorModel() {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (getCause() instanceof HttpClientErrorException hce && hce.getStatusCode() instanceof HttpStatus) {
            status = ((HttpStatus) hce.getStatusCode());
        }

        return new ErrorModel(status, getMessage());
    }
}
