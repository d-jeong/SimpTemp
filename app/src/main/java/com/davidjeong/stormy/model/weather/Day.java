package com.davidjeong.stormy.model.weather;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Day {
    private String mIcon;
    private long mTime;
    private double mTemperatureMin;
    private double mTemperatureMax;
    private double mPrecipitation;
    private String mSummary;
    private String mTimezone;

    public Day(String icon, long time, double temperatureMin, double temperatureMax, double precipitation, String summary, String timezone) {
        mIcon = icon;
        mTime = time;
        mTemperatureMin = temperatureMin;
        mTemperatureMax = temperatureMax;
        mPrecipitation = precipitation;
        mSummary = summary;
        mTimezone = timezone;
    }

    public long getTime() {
        return mTime;
    }

    /**
     * This function uses the UNIX timestamp stored in this object to form a
     * human-readable time
     *
     * @return time string in the form of "h:mm a" (EX: 5 PM)
     */
    public String getFormattedTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/DD EEEE");
        formatter.setTimeZone(TimeZone.getTimeZone(mTimezone));
        Date dateTime = new Date(mTime * 1000);
        String timeString = formatter.format(dateTime);

        return timeString;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public int getTemperatureMin() {
        return (int) Math.round(mTemperatureMin);
    }

    public void setTemperatureMin(double temperatureMin) {
        mTemperatureMin = temperatureMin;
    }

    public int getTemperatureMax() {
        return (int) Math.round(mTemperatureMax);
    }

    public void setTemperatureMax(double temperatureMax) {
        mTemperatureMax = temperatureMax;
    }

    public int getPrecipitation() {
        return (int) Math.round(mPrecipitation * 100);
    }

    public void setPrecipitation(double precipitation) {
        mPrecipitation = precipitation;
    }

    public String getIcon() {
        return mIcon;
    }

    public int getIconId() {
        return Forecast.getIconId(mIcon);
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getTimezone() {
        return mTimezone;
    }

    public void setTimezone(String timezone) {
        mTimezone = timezone;
    }
}
