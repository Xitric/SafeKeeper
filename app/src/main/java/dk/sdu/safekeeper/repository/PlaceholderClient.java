package dk.sdu.safekeeper.repository;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dk.sdu.privacyenforcer.client.PrivacyEnforcingClient;
import dk.sdu.privacyenforcer.client.filters.AbortAllFilter;
import dk.sdu.privacyenforcer.client.filters.FineLocationFilter;
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
                    .baseUrl("https://localhost:44330/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(createClient(context))
                    .build();
        }

        return instance;
    }

    private static OkHttpClient createClient(Context context) {
        PrivacyEnforcingClient privacyClient = new PrivacyEnforcingClient(context);

        privacyClient.registerFilter("location", new FineLocationFilter());
//        privacyClient.registerFilter("all", new AbortAllFilter());

        return privacyClient.getClientBuilder()
                .build();
    }

    public static PlaceholderService getService(Context context) {
        return getInstance(context).create(PlaceholderService.class);
    }
}
