package dk.sdu.safekeeper;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import java.io.IOException;
import java.util.List;

import dk.sdu.privacyenforcer.ui.PrivacyActivity;
import dk.sdu.safekeeper.repository.weather.WeatherResponse;

public class WeatherActivity extends PrivacyActivity {

    private TextView lblWeatherLocation;
    private TextView lblWeather;
    private TextView lblWeatherDescription;
    private TextView lblWeatherTemperature;
    private TextView lblWeatherHumidity;

    private WeatherViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        lblWeatherLocation = findViewById(R.id.lbl_weather_location);
        lblWeather = findViewById(R.id.lbl_weather);
        lblWeatherDescription = findViewById(R.id.lbl_weather_description);
        lblWeatherTemperature = findViewById(R.id.lbl_weather_temperature);
        lblWeatherHumidity = findViewById(R.id.lbl_weather_humidity);

        viewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
        viewModel.getStatus().observe(this, status -> Toast.makeText(this, status, Toast.LENGTH_SHORT).show());
        viewModel.getWeather().observe(this, weather -> {
            lblWeather.setText(weather.getWeatherName());
            lblWeatherDescription.setText(toTitleCase(weather.getDescription()));
            lblWeatherTemperature.setText(getString(R.string.temperature, weather.getTemperature()));
            lblWeatherHumidity.setText(getString(R.string.humidity, weather.getHumidity()));
        });
        viewModel.getAddress().observe(this, address -> lblWeatherLocation.setText(getString(R.string.weather_location, address)));
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            viewModel.init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        viewModel.init();
    }

    private String toTitleCase(String s) {
        String firstCharacter = s.substring(0, 1);
        return s.replaceFirst(firstCharacter, firstCharacter.toUpperCase());
    }
}
