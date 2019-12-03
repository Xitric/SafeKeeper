package dk.sdu.safekeeper.repository.weather;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.math3.distribution.NormalDistribution;

import dk.sdu.privacyenforcer.client.PrivacyEnforcingClient;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.annotations.EverythingIsNonNull;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SimulatedOpenWeatherClient {

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
                .addInterceptor(new SimulatorInterceptor())
                .build();
    }

    public static OpenWeatherService getService(Context context) {
        return getInstance(context).create(OpenWeatherService.class);
    }

    private static final class SimulatorInterceptor implements Interceptor {

        private NormalDistribution norm = new NormalDistribution(29.77, 4.36);

        @Override
        @EverythingIsNonNull
        public Response intercept(Chain chain) {
            try {
                Thread.sleep((int) norm.sample());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return new Response.Builder()
                    .code(200)
                    .message("Success")
                    .body(ResponseBody.create(MediaType.parse("application/json"), "{\n" +
                            "    \"coord\": {\n" +
                            "        \"lon\": 12,\n" +
                            "        \"lat\": 55\n" +
                            "    },\n" +
                            "    \"weather\": [\n" +
                            "        {\n" +
                            "            \"id\": 804,\n" +
                            "            \"main\": \"Clouds\",\n" +
                            "            \"description\": \"overcast clouds\",\n" +
                            "            \"icon\": \"04d\"\n" +
                            "        }\n" +
                            "    ],\n" +
                            "    \"base\": \"stations\",\n" +
                            "    \"main\": {\n" +
                            "        \"temp\": 278.57,\n" +
                            "        \"pressure\": 1004,\n" +
                            "        \"humidity\": 93,\n" +
                            "        \"temp_min\": 278.15,\n" +
                            "        \"temp_max\": 279.26\n" +
                            "    },\n" +
                            "    \"visibility\": 5000,\n" +
                            "    \"wind\": {\n" +
                            "        \"speed\": 4.1,\n" +
                            "        \"deg\": 180\n" +
                            "    },\n" +
                            "    \"clouds\": {\n" +
                            "        \"all\": 100\n" +
                            "    },\n" +
                            "    \"dt\": 1574779343,\n" +
                            "    \"sys\": {\n" +
                            "        \"type\": 1,\n" +
                            "        \"id\": 1588,\n" +
                            "        \"country\": \"DK\",\n" +
                            "        \"sunrise\": 1574751844,\n" +
                            "        \"sunset\": 1574780080\n" +
                            "    },\n" +
                            "    \"timezone\": 3600,\n" +
                            "    \"id\": 2615932,\n" +
                            "    \"name\": \"Nyrad\",\n" +
                            "    \"cod\": 200\n" +
                            "}"))
                    .request(chain.request())
                    .protocol(Protocol.HTTP_2)
                    .build();
        }
    }
}
