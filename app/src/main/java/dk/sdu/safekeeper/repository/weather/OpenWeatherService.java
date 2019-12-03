package dk.sdu.safekeeper.repository.weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherService {

    @GET("weather")
    Call<WeatherResponse> getWeatherHere(@Query("lat") double latitude, @Query("lon") double longitude);
}
