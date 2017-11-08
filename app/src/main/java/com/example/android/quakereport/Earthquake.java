package com.example.android.quakereport;

/**
 * Created by kahra on 8.11.2017.
 */

public class Earthquake {
    private double magnitude;
    private String location;
    private Long time;
    private String website;

    public Earthquake(double magnitude, String location, Long time, String website) {
        this.magnitude = magnitude;
        this.location = location;
        this.time = time;
        this.website = website;
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

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public String toString() {
        return "Earthquake{" +
                "magnitude=" + magnitude +
                ", location='" + location + '\'' +
                ", time=" + time +
                ", website='" + website + '\'' +
                '}';
    }
}
