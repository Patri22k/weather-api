package io.github.patri22k.weatherapi.model;

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
    public String datetime;
    public double feelslike;
    public double humidity;

    public static WeatherModel createWeatherModel(RawWeatherModel rw) {
        CurrentConditionsModel cc = rw.getCurrentConditions();

        return new WeatherModel(
                rw.getAddress(),
                rw.getDescription(),
                cc.getTemp(),
                cc.getDatetime(),
                cc.getFeelslike(),
                cc.getHumidity()
        );
    }

}
