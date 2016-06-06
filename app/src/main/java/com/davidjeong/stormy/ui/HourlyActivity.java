package com.davidjeong.stormy.ui;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.davidjeong.stormy.R;
import com.davidjeong.stormy.adapters.HourAdapter;
import com.davidjeong.stormy.model.weather.Hour;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HourlyActivity extends AppCompatActivity {

    private Hour[] mHours;

    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.locationLabel) TextView mLocationLabel;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        String location = intent.getStringExtra("CurrentLocation");
        mLocationLabel.setText(location);

        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.HOURLY_FORECAST);
        mHours = Arrays.copyOf(parcelables, parcelables.length, Hour[].class);

        HourAdapter adapter = new HourAdapter(mHours);
        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);
    }
}
