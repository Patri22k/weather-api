package io.github.patri22k.weatherapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentConditionsModel {

    private String datetime;

    private double temp;
    private double feelslike;
    private double humidity;

    @JsonSetter("temp")
    public void setTemp(double temp) {
        this.temp = toCelsius(temp);
    }

    @JsonSetter("feelslike")
    public void setFeelslike(double feelslike) {
        this.feelslike = toCelsius(feelslike);
    }

    public double toCelsius(double t) {
        double celsius = (5d / 9d) * (t - 32d);
        return (double) Math.round((celsius * 10)) / 10;
    }
}
