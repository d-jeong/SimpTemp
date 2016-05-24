package com.davidjeong.stormy;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private CurrentWeather mCurrentWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create the url that will be used for the http call
        String apiKey = "bf4d36c47381c45990e059d3a65f3a28";
        double latitude = 37.8267;
        double longitude = -122.423;
        String forecastUrl = "https://api.forecast.io/forecast/" + apiKey +
                "/" + latitude + "," + longitude;

        // check if the network is available before continuing
        if (isNetworkAvailable()) {

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

                }

                @Override
                public void onResponse(Call call, Response response) {
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);

                        if (response.isSuccessful()) {
                            mCurrentWeather = getCurrentDetails(jsonData);
                            Log.d(TAG, "Current Time:" + mCurrentWeather.getFormattedTime());
                        }
                        else {
                            alertUserAboutResponseError();
                        }
                    }
                    catch (IOException ioe) {
                        Log.e(TAG, "Exception caught: ", ioe);
                    }
                    catch (JSONException jsone) {
                        Log.e(TAG, "Exception caught: ", jsone);                    }
                }
            });
        }
        else {
            alertUserAboutNetworkError();
        }

        Log.d(TAG, "The Main UI code is running!");
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
}