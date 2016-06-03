package com.davidjeong.stormy.ui;

import android.content.Context;
import android.content.Intent;
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
import com.davidjeong.stormy.model.weather.Current;
import com.davidjeong.stormy.model.location.CurrentLocation;
import com.davidjeong.stormy.model.location.LocationProvider;
import com.davidjeong.stormy.model.weather.Day;
import com.davidjeong.stormy.model.weather.Forecast;
import com.davidjeong.stormy.model.weather.Hour;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements LocationProvider.LocationCallback {

    public static final String TAG = MainActivity.class.getSimpleName();

    private Forecast mForecast;
    private CurrentLocation mCurrentLocation;
    private LocationProvider mLocationProvider;

    // Butter Knife for boiler-plate code
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

        // progress bar should only be visible on refresh
        mProgressBar.setVisibility(View.INVISIBLE);

        // initialize access to GPS (Google's Location Services)
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
        mLocationProvider.disconnect();
        mLocationProvider.connect();
    }


    /**
     * This function takes a latitude and longitude coordinate and sets the current weather content
     * data on the main activity layout. If network is not available, it sends an error message
     * to the user.
     *
     * @param latitude  double that represents latitude
     * @param longitude double that represents longitude
     * @throws IOException
     */
    private void getForecast(double latitude, double longitude) throws IOException {
        // create the url that will be used for the http call
        String apiKey = "bf4d36c47381c45990e059d3a65f3a28";

        // use Geocoder to acquire the city and state names (or country if not in US)
        Geocoder gcd = new Geocoder(this, Locale.getDefault());

        List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        mCurrentLocation = new CurrentLocation(latitude, longitude, city, state, country);

        // url to be sent to forecast.io's API
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
                    // setting the visibility of progress bar is done on the ui thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToggleRefresh();
                        }
                    });

                    alertUserAboutResponseError();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String jsonData = response.body().string();

                    if (response.isSuccessful()) {
                        try {
                            // parses the jsonData returned to store it into the data struct
                            mForecast = getForecastDetails(jsonData);
                        } catch (JSONException e) {
                            Log.e(TAG, "Error occured: " + e);
                            e.printStackTrace();
                        }

                        // ensures that layout update occurs on the ui thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateDisplay();
                            }
                        });
                    } else {
                        alertUserAboutResponseError();
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


    /**
     * This function toggles the visibility between the progress bar and the
     * refresh image (button) when called on
     */
    private void ToggleRefresh() {
        if (mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }
    }


    /**
     * This function updates the main activity with the current weather data
     * that was retrieved through the http call
     */
    private void updateDisplay() {
        Current current = mForecast.getCurrent();

        mTemperatureLabel.setText(String.valueOf(current.getTemperature()));
        mTimeLabel.setText("At " + current.getFormattedTime());
        mHumidityValue.setText(String.valueOf(current.getHumidity()));
        mPrecipValue.setText(current.getPrecipitation() + "%");
        mSummaryLabel.setText("It is currently " + current.getSummary().toLowerCase());

        // If abbreviation for state exists, uses it
        // Else, uses the country name
        if (CurrentLocation.STATES.containsKey(mCurrentLocation.getState())) {
            mLocationLabel.setText(mCurrentLocation.getCity() + ", " +
                    CurrentLocation.STATES.get(mCurrentLocation.getState()));
        } else {
            mLocationLabel.setText(mCurrentLocation.getCity() + ", " +
                    mCurrentLocation.getCountry());
        }

        Drawable drawable = ContextCompat.getDrawable(this, current.getIconId());
        mIconImageView.setImageDrawable(drawable);
    }


    private Forecast getForecastDetails(String jsonData) throws JSONException {
        Current current = getCurrentDetails(jsonData);
        Hour[] hourly = getHourlyDetails(jsonData);
        Day[] daily = getDailyDetails(jsonData);

        Forecast forecast = new Forecast(current, hourly, daily);

        return forecast;
    }


    private Day[] getDailyDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");

        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray dailyData = daily.getJSONArray("data");

        Day[] dailyWeather = new Day[dailyData.length()];

        for (int i = 0; i < dailyData.length(); ++i) {
            JSONObject JSONDay = dailyData.getJSONObject(i);
            dailyWeather[i] = new Day(
                    JSONDay.getString("icon"),
                    JSONDay.getLong("time"),
                    JSONDay.getDouble("temperatureMin"),
                    JSONDay.getDouble("temperatureMax"),
                    JSONDay.getDouble("precipProbability"),
                    JSONDay.getString("summary"),
                    timezone
            );
        }

        return dailyWeather;
    }


    private Hour[] getHourlyDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");

        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray hourlyData = hourly.getJSONArray("data");

        Hour[] hourlyWeather = new Hour[hourlyData.length()];

        for (int i = 0; i < hourlyData.length(); ++i) {
            JSONObject JSONHour = hourlyData.getJSONObject(i);
            hourlyWeather[i] = new Hour(
                    JSONHour.getString("icon"),
                    JSONHour.getLong("time"),
                    JSONHour.getDouble("temperature"),
                    JSONHour.getDouble("precipProbability"),
                    JSONHour.getString("summary"),
                    timezone
            );
        }

        return hourlyWeather;
    }

    /**
     * This function takes the the JSON data retrieved from forecast.io's API and
     * stores the necessary info on a Current Object that is to be returned
     *
     * @param jsonData the data retrieved from forecast.io's API; contains weather data
     * @return a Current Object that holds current weather info such as temp, time, etc.
     * @throws JSONException
     */
    private Current getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");

        JSONObject currently = new JSONObject(forecast.getString("currently"));

        Current current = new Current(
                currently.getString("icon"),
                currently.getLong("time"),
                currently.getDouble("temperature"),
                currently.getDouble("humidity"),
                currently.getDouble("precipProbability"),
                currently.getString("summary"),
                timezone
        );

        return current;
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

        // Handles the new location by passing in the latitude/longitude to be used
        // to make the http call. The retrieved data is used to update the display
        try {
            getForecast(latitude, longitude);
        } catch (IOException e) {
            Log.e(TAG, "Error occured: " + e);
            e.printStackTrace();
        }
    }

    @OnClick(R.id.dailyButton)
    public void startDailyActivity(View view) {
        Intent intent = new Intent(this, DailyActivity.class);
        startActivity(intent);
    }
}