package dk.sdu.safekeeper;

import android.app.Application;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.io.IOException;
import java.util.List;

import dk.sdu.safekeeper.repository.weather.OpenWeatherClient;
import dk.sdu.safekeeper.repository.weather.OpenWeatherService;
import dk.sdu.safekeeper.repository.weather.WeatherResponse;
import okhttp3.internal.annotations.EverythingIsNonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherViewModel extends AndroidViewModel {

    private OpenWeatherService weatherService;
    private Geocoder geocoder;
    private LocationManager locationManager;

    private MutableLiveData<String> status = new MutableLiveData<>();
    private MutableLiveData<Location> location = new MutableLiveData<>();
    private LiveData<WeatherResponse> weather = Transformations.switchMap(location, location -> queryWeather());
    private LiveData<String> address = Transformations.map(weather, weather -> queryAddress());

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        weatherService = OpenWeatherClient.getService(application);
        geocoder = new Geocoder(application);
        locationManager = (LocationManager) application.getSystemService(Context.LOCATION_SERVICE);
    }

    public void init() {
        if (location.getValue() != null) return;

        try {
            Location lastKnown = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnown != null) {
                location.postValue(lastKnown);
                return;
            }

            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationListener() {
                @Override
                public void onLocationChanged(Location loc) {
                    location.postValue(loc);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            }, null);
        } catch (SecurityException ignored) {
        }
    }

    private LiveData<WeatherResponse> queryWeather() {
        MutableLiveData<WeatherResponse> weatherResponse = new MutableLiveData<>();
        if (location.getValue() == null) return weatherResponse;

        weatherService.getWeatherHere(location.getValue().getLatitude(), location.getValue().getLongitude()).enqueue(new Callback<WeatherResponse>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    weatherResponse.postValue(response.body());
                } else {
                    onFailure(call, new IllegalStateException());
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                status.postValue("Failed to get weather data");
            }
        });

        return weatherResponse;
    }

    private String queryAddress() {
        if (weather.getValue() == null) return "Unknown";

        try {
            List<Address> addresses = geocoder.getFromLocation(weather.getValue().getLatitude(), weather.getValue().getLongitude(), 1);
            if (addresses.size() != 0) {
                return addresses.get(0).getThoroughfare();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        status.postValue("Failed to infer your address");
        return "Unknown";
    }

    public LiveData<String> getStatus() {
        return status;
    }

    public LiveData<WeatherResponse> getWeather() {
        return weather;
    }

    public LiveData<String> getAddress() {
        return address;
    }
}
