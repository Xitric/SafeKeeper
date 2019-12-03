package dk.sdu.safekeeper.repository.messenger;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import dk.sdu.privacyenforcer.client.PrivacyEnforcingClient;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
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
                .addInterceptor(new EchoInterceptor())
                .build();
    }

    public static MessengerService getService(Context context) {
        return getInstance(context).create(MessengerService.class);
    }

    private static final class EchoInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            String bodyJson = "{}";
            RequestBody requestBody = chain.request().body();

            try (Buffer buffer = new Buffer()) {
                if (requestBody != null) {
                    requestBody.writeTo(buffer);
                    bodyJson = buffer.readUtf8();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return new Response.Builder()
                    .code(200)
                    .message("Success")
                    .body(ResponseBody.create(MediaType.parse("application/json"), bodyJson))
                    .request(chain.request())
                    .protocol(Protocol.HTTP_2)
                    .build();
        }
    }
}
