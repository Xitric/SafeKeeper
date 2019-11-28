package dk.sdu.safekeeper.repository.messenger;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dk.sdu.privacyenforcer.client.PrivacyEnforcingClient;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This is an incomplete Messenger client. It is not intended to work, but simply to show how
 * data is intercepted when sent as part of a request body.
 */
public class MessengerClient {

    private static Retrofit instance;

    public static Retrofit getInstance(Context context) {
        if (instance == null) {
            Gson gson = new GsonBuilder()
                    .create();

            instance = new Retrofit.Builder()
                    .baseUrl("https://graph.facebook.com/v5.0/")
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

    public static MessengerService getService(Context context) {
        return getInstance(context).create(MessengerService.class);
    }
}
