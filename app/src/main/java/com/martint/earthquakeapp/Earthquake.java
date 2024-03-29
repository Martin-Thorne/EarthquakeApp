package com.martint.earthquakeapp;

/**
 * An Earthquake object that contains information on a single earthquake
 */
public class Earthquake {

    // The latitude of the earthquake
    private double latitude;

    //The longitude of the earthquake
    private double longitude;

    // The location of the earthquake
    private String location;

    // The magnitude of the earthquake
    private double magnitude;

    // The webpage for the earthquake
    private String url;

    /**
     * Constructs a new Earthquake object
     *
     * @param latitude  is the latitude of the earthquake
     * @param longitude is the longitude of the earthquake
     * @param location  is the location of the earthquake
     * @param magnitude is the magnitude of the earthquake
     * @param url       is the webpage for the earthquake
     */
    public Earthquake(double latitude, double longitude, String location, double magnitude, String url) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.magnitude = magnitude;
        this.url = url;
    }

    /**
     * @return the latitude of the earthquake
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @return the longitude of the earthquake
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @return the location of the earthquake
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return the magnitude of the earthquake
     */
    public double getMagnitude() {
        return magnitude;
    }

    /**
     * @return the webpage for the earthquake
     */
    public String getUrl() {
        return url;
    }
}
