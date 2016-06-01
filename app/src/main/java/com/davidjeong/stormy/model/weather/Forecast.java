package com.davidjeong.stormy.model.weather;

public class Forecast {
    private Current mCurrent;
    private Hour[] mHourlyWeather;
    private Day[] mDailyWeather;


    public Forecast(Current current, Hour[] hourlyWeather, Day[] dailyWeather) {
        mCurrent = current;
        mHourlyWeather = hourlyWeather;
        mDailyWeather = dailyWeather;
    }

    public Current getCurrent() {
        return mCurrent;
    }

    public void setCurrent(Current current) {
        mCurrent = current;
    }

    public Hour[] getHourlyWeather() {
        return mHourlyWeather;
    }

    public void setHourlyWeather(Hour[] hourlyWeather) {
        mHourlyWeather = hourlyWeather;
    }

    public Day[] getDailyWeather() {
        return mDailyWeather;
    }

    public void setDailyWeather(Day[] dailyWeather) {
        mDailyWeather = dailyWeather;
    }
}
