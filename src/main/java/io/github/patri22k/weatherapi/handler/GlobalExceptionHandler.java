package io.github.patri22k.weatherapi.handler;

import io.github.patri22k.weatherapi.exception.WeatherAPIRequestException;
import io.github.patri22k.weatherapi.model.ErrorModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WeatherAPIRequestException.class)
    public ResponseEntity<ErrorModel> handleWeatherAPIException(WeatherAPIRequestException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorModel(400, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorModel> handleGenericException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorModel(500, ex.getMessage()));
    }
}
