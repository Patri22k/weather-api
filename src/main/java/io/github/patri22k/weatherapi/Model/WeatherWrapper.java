package io.github.patri22k.weatherapi.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WeatherWrapper {

    protected String source;
    protected WeatherModel data;

}
