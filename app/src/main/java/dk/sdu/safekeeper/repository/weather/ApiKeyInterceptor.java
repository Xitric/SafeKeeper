package dk.sdu.safekeeper.repository.weather;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ApiKeyInterceptor implements Interceptor {

    @Override
    @NonNull
    public Response intercept(@NonNull Chain chain) throws IOException {
        //Add API key information to all outgoing requests
        HttpUrl url = chain.request().url().newBuilder()
                .addQueryParameter("appid", "957b78a98cc29e38aea760366ff62a82")
                .build();

        Request newRequest = chain.request().newBuilder().url(url).build();
        return chain.proceed(newRequest);
    }
}
