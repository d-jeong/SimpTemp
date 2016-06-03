package com.davidjeong.stormy.model.weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Day implements Parcelable {
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
     * @return time string in the form of "MM/DD EEEE" (EX: 06/01 Wednesday)
     */
    public String getFormattedTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/d EEE");
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

    // makes the Day object parcelable
    @Override
    public int describeContents() {
        // unused
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mIcon);
        dest.writeLong(mTime);
        dest.writeDouble(mTemperatureMax);
        dest.writeDouble(mTemperatureMin);
        dest.writeDouble(mPrecipitation);
        dest.writeString(mSummary);
        dest.writeString(mTimezone);
    }

    private Day(Parcel in) {
        mIcon = in.readString();
        mTime = in.readLong();
        mTemperatureMax = in.readDouble();
        mTemperatureMin = in.readDouble();
        mPrecipitation = in.readDouble();
        mSummary = in.readString();
        mTimezone = in.readString();
    }

    public static final Creator<Day> CREATOR = new Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel source) {
            return new Day(source);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };
}
