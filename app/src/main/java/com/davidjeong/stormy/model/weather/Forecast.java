package com.davidjeong.stormy.model.weather;

import com.davidjeong.stormy.R;

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

    public static int getIconId(String iconString) {
        int iconId = R.drawable.clear_day;

        if (iconString.equals("clear-day")) {
            iconId = R.drawable.clear_day;
        }
        else if (iconString.equals("clear-night")) {
            iconId = R.drawable.clear_night;
        }
        else if (iconString.equals("rain")) {
            iconId = R.drawable.rain;
        }
        else if (iconString.equals("snow")) {
            iconId = R.drawable.snow;
        }
        else if (iconString.equals("sleet")) {
            iconId = R.drawable.sleet;
        }
        else if (iconString.equals("wind")) {
            iconId = R.drawable.wind;
        }
        else if (iconString.equals("fog")) {
            iconId = R.drawable.fog;
        }
        else if (iconString.equals("cloudy")) {
            iconId = R.drawable.cloudy;
        }
        else if (iconString.equals("partly-cloudy-day")) {
            iconId = R.drawable.partly_cloudy;
        }
        else if (iconString.equals("partly-cloudy-night")) {
            iconId = R.drawable.cloudy_night;
        }

        return iconId;
    }
}
