package dk.sdu.safekeeper.repository.weather;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dk.sdu.privacyenforcer.client.PrivacyEnforcingClient;
import dk.sdu.safekeeper.repository.PlaceholderService;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenWeatherClient {

    private static Retrofit instance;

    public static Retrofit getInstance(Context context) {
        if (instance == null) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(WeatherResponse.class, new WeatherResponse.WeatherResponseDeserializer())
                    .create();

            instance = new Retrofit.Builder()
                    .baseUrl("http://api.openweathermap.org/data/2.5/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(createClient(context))
                    .build();
        }

        return instance;
    }

    private static OkHttpClient createClient(Context context) {
        PrivacyEnforcingClient privacyClient = new PrivacyEnforcingClient(context);
        return privacyClient.getClientBuilder()
                .addInterceptor(new ApiKeyInterceptor())
                .build();
    }

    public static OpenWeatherService getService(Context context) {
        return getInstance(context).create(OpenWeatherService.class);
    }
}
