package dk.sdu.privacyenforcer.client;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

class PrivacyRequestInterceptor implements Interceptor {

    private final RequestBodyParser parser;
    private final FilterEngine filterEngine;

    PrivacyRequestInterceptor(FilterEngine filterEngine) {
        this.parser = new RequestBodyParser();
        this.filterEngine = filterEngine;
    }

    @Override
    @NonNull
    public Response intercept(@NonNull Chain chain) throws IOException {
        okhttp3.RequestBody originalBody = chain.request().body();

        RequestUrl url = new RequestUrl(chain.request().url());
        JSONObject body = parser.toJson(originalBody);
        ViolationCollection violations = filterEngine.applyFilters(url, body);

        if (violations.isAborted()) {
            return new Response.Builder()
                    .code(404)
                    .protocol(Protocol.HTTP_2)
                    .message("Request not allowed")
                    .request(chain.request())
                    .body(ResponseBody.create(MediaType.parse("application/json"), "Body"))
                    .build();
        }

        violations.resolve();

        Request.Builder newRequestBuilder = chain.request().newBuilder().url(url.getUrl());
        if (body != null) {
            okhttp3.RequestBody newBody = parser.toHttpBody(body);
            newRequestBuilder.method(chain.request().method(), newBody);
        }
        return chain.proceed(newRequestBuilder.build());
    }
}
