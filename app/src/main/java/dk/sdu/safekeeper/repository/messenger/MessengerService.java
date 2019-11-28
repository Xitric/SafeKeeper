package dk.sdu.safekeeper.repository.messenger;

import dk.sdu.safekeeper.repository.weather.WeatherResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MessengerService {

    @POST("me")
    Call<WeatherResponse> sendLocationData(@Body WeatherResponse data);
}
