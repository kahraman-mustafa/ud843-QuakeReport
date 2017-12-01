package com.example.android.quakereport;

/**
 * Created by kahra on 8.11.2017.
 */

public class Earthquake {
    private double magnitude;
    private String location;
    private Long timeInMillisec;
    private String url;

    public Earthquake(double magnitude, String location, Long timeInMillisec, String url) {
        this.magnitude = magnitude;
        this.location = location;
        this.timeInMillisec = timeInMillisec;
        this.url = url;
    }

    public Earthquake() {

    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getTimeInMillisec() {
        return timeInMillisec;
    }

    public void setTimeInMillisec(Long timeInMillisec) {
        this.timeInMillisec = timeInMillisec;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Earthquake{" +
                "magnitude=" + magnitude +
                ", location='" + location + '\'' +
                ", timeInMillisec=" + timeInMillisec +
                ", url='" + url + '\'' +
                '}';
    }
}
