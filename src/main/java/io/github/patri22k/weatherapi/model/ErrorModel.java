package io.github.patri22k.weatherapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorModel {
    private HttpStatus status;
    private String error;

    public ResponseEntity<ErrorModel> toResponseEntity() {
        return ResponseEntity.status(status).body(this);
    }
}
