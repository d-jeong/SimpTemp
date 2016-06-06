package com.davidjeong.stormy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.davidjeong.stormy.R;
import com.davidjeong.stormy.model.weather.Forecast;
import com.davidjeong.stormy.model.weather.Hour;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder> {

    private Hour[] mHours;
    private final char degree = '\u00B0';

    public HourAdapter(Hour[] hours) {
        mHours = hours;
    }

    @Override
    public HourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hourly_list_item, parent, false);

        HourViewHolder viewHolder = new HourViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HourViewHolder holder, int position) {
        holder.bindHour(mHours[position]);
    }

    @Override
    public int getItemCount() {
        return mHours.length;
    }


    public class HourViewHolder extends RecyclerView.ViewHolder {

        public ImageView mIconImageView;
        public TextView mPrecipValue;
        public TextView mTimeLabel;
        public TextView mTemperatureLabel;
        public TextView mSummaryLabel;

        public HourViewHolder(View itemView) {
            super(itemView);

            mIconImageView = (ImageView) itemView.findViewById(R.id.iconImageView);
            mPrecipValue = (TextView) itemView.findViewById(R.id.precipValue);
            mTimeLabel = (TextView) itemView.findViewById(R.id.timeLabel);
            mTemperatureLabel = (TextView) itemView.findViewById(R.id.temperatureLabel);
            mSummaryLabel = (TextView) itemView.findViewById(R.id.summaryLabel);
        }

        public void bindHour(Hour hour) {
            mIconImageView.setImageResource(hour.getIconId());
            mPrecipValue.setText(String.valueOf(hour.getPrecipitation()) + "%");
            mTimeLabel.setText(hour.getFormattedTime());
            mTemperatureLabel.setText(String.valueOf(hour.getTemperature()) + degree);
            mSummaryLabel.setText(hour.getSummary());

        }
    }
}
