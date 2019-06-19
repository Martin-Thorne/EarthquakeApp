package com.martint.earthquakeapp;

/**
 * An Earthquake object that contains information on a single earthquake
 */
public class Earthquake {

    // The latitude of the earthquake
    private double mLatitude;

    //The longitude of the earthquake
    private double mLongitude;

    /**
     * Constructs a new Earthquake object
     *
     * @param latitude  is the latitude of the earthquake
     * @param longitude is the longitude of the earthquake
     */
    public Earthquake(double latitude, double longitude) {
        mLatitude = latitude;
        mLongitude = longitude;
    }

    /**
     * @return the latitude of the earthquake
     */
    public double getmLatitude() {
        return mLatitude;
    }

    /**
     * @return the longitude of the earthquake
     */
    public double getmLongitude() {
        return mLongitude;
    }
}
