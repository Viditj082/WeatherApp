package com.example.weather;

public class Forecast_model {

    private String time,icon,temperature,expected;


    public Forecast_model(String time, String icon, String temperature,String expected) {
        this.time = time;
        this.icon = icon;
        this.temperature = temperature;
        this.expected=expected;
    }

    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
