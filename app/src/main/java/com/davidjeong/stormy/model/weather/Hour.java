package com.davidjeong.stormy.model.weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Hour implements Parcelable {
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

    public int getTemperature() {
        return (int) Math.round(mTemperature);
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
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


    // makes the Hour object parcelable
    @Override
    public int describeContents() {
        // unused
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mIcon);
        dest.writeLong(mTime);
        dest.writeDouble(mTemperature);
        dest.writeDouble(mPrecipitation);
        dest.writeString(mSummary);
        dest.writeString(mTimezone);
    }

    private Hour(Parcel in) {
        mIcon = in.readString();
        mTime = in.readLong();
        mTemperature = in.readDouble();
        mPrecipitation = in.readDouble();
        mSummary = in.readString();
        mTimezone = in.readString();
    }

    public static final Creator<Hour> CREATOR = new Creator<Hour>() {
        @Override
        public Hour createFromParcel(Parcel source) {
            return new Hour(source);
        }

        @Override
        public Hour[] newArray(int size) {
            return new Hour[size];
        }
    };
}
