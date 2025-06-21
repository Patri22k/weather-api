package io.github.patri22k.weatherapi.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentConditionsModel {

    private double temp;

    public double convertToCelsius() {
        double celsius = (5d / 9d) * (temp - 32d);
        return (double) Math.round((celsius * 100)) / 100;
    }
}
