package io.github.patri22k.weatherapi.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WeatherModel {

    public String address;
    public String description;
    public double temp;

    public static WeatherModel createWeatherModel(RawWeatherModel rw) {
        return new WeatherModel(
                rw.getAddress(),
                rw.getDescription(),
                rw.getCurrentConditions().convertToCelsius()
        );
    }

}
