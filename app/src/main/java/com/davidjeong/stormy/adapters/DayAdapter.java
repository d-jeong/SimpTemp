package com.davidjeong.stormy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.davidjeong.stormy.R;
import com.davidjeong.stormy.model.weather.Day;

public class DayAdapter extends BaseAdapter {

    private Context mContext;
    private Day[] mDays;
    private final char degree = '\u00B0';

    public DayAdapter(Context context, Day[] days) {
        mContext = context;
        mDays = days;
    }

    @Override
    public int getCount() {
        return mDays.length;
    }

    @Override
    public Object getItem(int position) {
        return mDays[position];
    }

    @Override
    public long getItemId(int position) {
        // unused. Normally tags item for easy reference
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            // brand new
            convertView = LayoutInflater.from(mContext).inflate(R.layout.daily_list_item, null);
            holder = new ViewHolder();
            holder.iconImageView = (ImageView) convertView.findViewById(R.id.iconImageView);
            holder.temperatureHighLabel = (TextView) convertView.findViewById(R.id.temperatureHighLabel);
            holder.temperatureLowLabel = (TextView) convertView.findViewById(R.id.temperatureLowLabel);
            holder.precipValue = (TextView) convertView.findViewById(R.id.precipValue);
            holder.dayLabel = (TextView) convertView.findViewById(R.id.dayLabel);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Day day = mDays[position];

        holder.iconImageView.setImageResource(day.getIconId());
        holder.temperatureHighLabel.setText(String.valueOf(day.getTemperatureMax()) + degree);
        holder.temperatureLowLabel.setText(String.valueOf(day.getTemperatureMin()) + degree);
        holder.precipValue.setText(day.getPrecipitation() + "%");
        holder.dayLabel.setText(day.getFormattedTime());

        return convertView;
    }

    private static class ViewHolder {
        // public by default since it's static
        ImageView iconImageView;
        TextView temperatureLowLabel;
        TextView temperatureHighLabel;
        TextView precipValue;
        TextView dayLabel;
    }
}
