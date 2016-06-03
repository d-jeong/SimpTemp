package com.davidjeong.stormy.model.weather;

import com.davidjeong.stormy.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Hour {
    private String mIcon;
    private long mTime;
    private double mTemperature;
    private double mPrecipitation;
    private String mSummary;
    private String mTimezone;


    public Hour(String icon, long time, double temperature, double precipitation, String summary, String timezone) {
        mIcon = icon;
        mTime = time;
        mTemperature = temperature;
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
        SimpleDateFormat formatter = new SimpleDateFormat("h a");
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

    public double getTemperature() {
        return mTemperature;
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public double getPrecipitation() {
        return mPrecipitation;
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
