package dk.sdu.privacyenforcer.client.mutators.k_anonymity_client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Http client used to query the TTP K-anonymity server.
 */
public class KAnonymityClient {

    private static Retrofit instance;

    public static Retrofit getInstance() {
        if (instance == null) {
            Gson gson = new GsonBuilder()
                    .create();

            instance = new Retrofit.Builder()
                    .baseUrl("http://safekeeper.azurewebsites.net/api/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(createClient())
                    .build();
        }

        return instance;
    }

    private static OkHttpClient createClient() {
        return new OkHttpClient.Builder().build();
    }

    public static KAnonymityService getService() {
        return getInstance().create(KAnonymityService.class);
    }
}
