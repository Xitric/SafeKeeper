package dk.sdu.safekeeper.repository;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dk.sdu.privacyenforcer.client.PrivacyEnforcingClient;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlaceholderClient {

    private static Retrofit instance;

    public static Retrofit getInstance(Context context) {
        if (instance == null) {
            Gson gson = new GsonBuilder()
                    .create();

            instance = new Retrofit.Builder()
                    .baseUrl("https://safekeeper.azurewebsites.net/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(createClient(context))
                    .build();
        }

        return instance;
    }

    private static OkHttpClient createClient(Context context) {
        PrivacyEnforcingClient privacyClient = new PrivacyEnforcingClient(context);
        return privacyClient.getClientBuilder()
                .build();
    }

    public static PlaceholderService getService(Context context) {
        return getInstance(context).create(PlaceholderService.class);
    }
}
