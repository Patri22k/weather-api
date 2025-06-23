package io.github.patri22k.weatherapi.service;

import io.github.patri22k.weatherapi.exception.WeatherAPIRequestException;
import io.github.patri22k.weatherapi.model.ErrorModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ErrorMapperService {

    public ErrorModel toErrorModel(Exception error) {
        if (error instanceof WeatherAPIRequestException wae) {
            return wae.createErrorModel();
        }

        return new ErrorModel(HttpStatus.INTERNAL_SERVER_ERROR, error.getMessage());
    }
}
