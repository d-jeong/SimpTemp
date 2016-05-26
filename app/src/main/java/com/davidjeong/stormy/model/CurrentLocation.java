package com.davidjeong.stormy.model;

public class CurrentLocation {

    private double mLatitude;
    private double mLongitude;
    private String mCity;
    private String mState;
    private String mCountry;

    public CurrentLocation(double latitude, double longitude, String city,
                           String state, String country) {
        mLatitude = latitude;
        mLongitude = longitude;
        mCity = city;
        mState = state;
        mCountry = country;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public String getCity() {
        return mCity;
    }

    public String getState() {
        return mState;
    }

    public String getCountry() {
        return mCountry;
    }
}
