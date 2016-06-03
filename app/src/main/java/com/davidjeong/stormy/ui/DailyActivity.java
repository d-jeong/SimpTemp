package com.davidjeong.stormy.ui;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.davidjeong.stormy.R;
import com.davidjeong.stormy.adapters.DayAdapter;
import com.davidjeong.stormy.model.weather.Day;

public class DailyActivity extends ListActivity {

    private Day[] mDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);

        DayAdapter adapter = new DayAdapter(this, mDays);
    }
}
