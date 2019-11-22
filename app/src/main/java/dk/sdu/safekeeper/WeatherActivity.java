package dk.sdu.safekeeper;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import dk.sdu.privacyenforcer.ui.PrivacyActivity;
import dk.sdu.safekeeper.repository.weather.OpenWeatherClient;
import dk.sdu.safekeeper.repository.weather.WeatherResponse;
import okhttp3.internal.annotations.EverythingIsNonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends PrivacyActivity {

    private TextView lblWeatherLocation;
    private TextView lblWeather;
    private TextView lblWeatherDescription;
    private TextView lblWeatherTemperature;
    private TextView lblWeatherHumidity;

    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        lblWeatherLocation = findViewById(R.id.lbl_weather_location);
        lblWeather = findViewById(R.id.lbl_weather);
        lblWeatherDescription = findViewById(R.id.lbl_weather_description);
        lblWeatherTemperature = findViewById(R.id.lbl_weather_temperature);
        lblWeatherHumidity = findViewById(R.id.lbl_weather_humidity);

        geocoder = new Geocoder(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();

        OpenWeatherClient.getService(getApplicationContext()).getWeatherHere(55.367558f, 10.431125f).enqueue(new Callback<WeatherResponse>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                WeatherResponse weather = response.body();

                if (weather != null) {
                    lblWeatherLocation.setText(getString(R.string.weather_location, getCity(weather)));
                    lblWeather.setText(weather.getWeatherName());
                    lblWeatherDescription.setText(toTitleCase(weather.getDescription()));
                    lblWeatherTemperature.setText(getString(R.string.temperature, weather.getTemperature()));
                    lblWeatherHumidity.setText(getString(R.string.humidity, weather.getHumidity()));
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                lblWeather.setText(R.string.weather_error);
            }
        });
    }

    private String toTitleCase(String s) {
        String firstCharacter = s.substring(0, 1);
        return s.replaceFirst(firstCharacter, firstCharacter.toUpperCase());
    }

    private String getCity(WeatherResponse weather) {
        try {
            List<Address> addresses = geocoder.getFromLocation(weather.getLatitude(), weather.getLongitude(), 1);
            if (addresses.size() != 0) {
                Address address = addresses.get(0);
                return address.getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "ERROR!";
    }
}
