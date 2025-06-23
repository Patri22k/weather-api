package io.github.patri22k.weatherapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RawWeatherModel {

    private String address;
    private String description;
    private CurrentConditionsModel currentConditions;

}
