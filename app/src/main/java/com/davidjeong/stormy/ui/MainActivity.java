package com.davidjeong.stormy.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.davidjeong.stormy.R;
import com.davidjeong.stormy.model.AlertDialogFragment;
import com.davidjeong.stormy.model.CurrentLocation;
import com.davidjeong.stormy.model.CurrentWeather;
import com.davidjeong.stormy.model.LocationProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements LocationProvider.LocationCallback {

    public static final String TAG = MainActivity.class.getSimpleName();

    private CurrentWeather mCurrentWeather;
    private CurrentLocation mCurrentLocation;
    private LocationProvider mLocationProvider;

    @BindView(R.id.locationLabel)
    TextView mLocationLabel;
    @BindView(R.id.timeLabel)
    TextView mTimeLabel;
    @BindView(R.id.temperatureLabel)
    TextView mTemperatureLabel;
    @BindView(R.id.humidityValue)
    TextView mHumidityValue;
    @BindView(R.id.precipValue)
    TextView mPrecipValue;
    @BindView(R.id.summaryLabel)
    TextView mSummaryLabel;
    @BindView(R.id.iconImageView)
    ImageView mIconImageView;
    @BindView(R.id.refreshImageView)
    ImageView mRefreshImageView;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mProgressBar.setVisibility(View.INVISIBLE);

        mLocationProvider = new LocationProvider(this, this);

        getForecast();

        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast();
            }
        });
    }

    private void getForecast() {
        mLocationProvider.connect();
    }

    private void getForecast(double latitude, final double longitude) {
        // create the url that will be used for the http call
        String apiKey = "bf4d36c47381c45990e059d3a65f3a28";

        // use Geocoder to acquire the city and state names (or country if not in US)
        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);
            String city =  addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            mCurrentLocation = new CurrentLocation(latitude, longitude, city, state, country);
        } catch (IOException e) {
            Log.e(TAG, "Exception caught: ", e);
        }

        String forecastUrl = "https://api.forecast.io/forecast/" + apiKey +
                "/" + latitude + "," + longitude;

        // check if the network is available before continuing
        if (isNetworkAvailable()) {

            ToggleRefresh();

            // set up the client and the call
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastUrl)
                    .build();

            Call call = client.newCall(request);

            // get the data asynchronously to ensure the user can continue to use the app
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToggleRefresh();
                        }
                    });
                    alertUserAboutResponseError();
                }

                @Override
                public void onResponse(Call call, Response response) {
                    try {
                        String jsonData = response.body().string();

                        if (response.isSuccessful()) {
                            mCurrentWeather = getCurrentDetails(jsonData);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                        } else {
                            alertUserAboutResponseError();
                        }
                    } catch (IOException | JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToggleRefresh();
                        }
                    });
                }
            });
        } else {
            alertUserAboutNetworkError();
        }
    }


    private void ToggleRefresh() {
        if (mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplay() {
        mTemperatureLabel.setText(String.valueOf(mCurrentWeather.getTemperature()));
        mTimeLabel.setText("At " + mCurrentWeather.getFormattedTime());
        mHumidityValue.setText(String.valueOf(mCurrentWeather.getHumidity()));
        mPrecipValue.setText(mCurrentWeather.getPrecipitation() + "%");
        mSummaryLabel.setText(mCurrentWeather.getSummary());

        if (mCurrentLocation.getCountry().equals("United States")) {
            mLocationLabel.setText(mCurrentLocation.getCity() + ", " + mCurrentLocation.getState());
        } else {
            mLocationLabel.setText(mCurrentLocation.getCity() + ", " + mCurrentLocation.getCountry());
        }



        Drawable drawable = ContextCompat.getDrawable(this, mCurrentWeather.getIconId());
        mIconImageView.setImageDrawable(drawable);
    }


    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");

        JSONObject currently = new JSONObject(forecast.getString("currently"));

        CurrentWeather currentWeather = new CurrentWeather(
                currently.getString("icon"),
                currently.getLong("time"),
                currently.getDouble("temperature"),
                currently.getDouble("humidity"),
                currently.getDouble("precipProbability"),
                currently.getString("summary"),
                timezone
        );

        return currentWeather;
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;

        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }


    private void alertUserAboutNetworkError() {
        AlertDialogFragment dialog = AlertDialogFragment.newInstance(
                getString(R.string.error_title),
                getString(R.string.network_error_message),
                getString(R.string.error_ok_button_text));
        dialog.show(getFragmentManager(), "error_dialog");

    }


    private void alertUserAboutResponseError() {
        AlertDialogFragment dialog = AlertDialogFragment.newInstance(
                getString(R.string.error_title),
                getString(R.string.response_error_message),
                getString(R.string.error_ok_button_text));
        dialog.show(getFragmentManager(), "error_dialog");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocationProvider.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationProvider.disconnect();
    }

    @Override
    public void handleNewLocation(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        getForecast(latitude, longitude);
    }
}