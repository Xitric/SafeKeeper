package dk.sdu.privacyenforcer.client;

import androidx.annotation.NonNull;

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
        parser = new RequestBodyParser();
        this.filterEngine = filterEngine;
    }

    @Override
    @NonNull
    public Response intercept(@NonNull Chain chain) throws IOException {
        okhttp3.RequestBody originalBody = chain.request().body();
        if (originalBody == null) {
            return chain.proceed(chain.request());
        }

        RequestBody context = parser.toInternalBody(originalBody);
        ViolationCollection violations = filterEngine.applyFilters(context);

        if (violations.isAborted()) {
            return new Response.Builder()
                    .code(404)
                    .protocol(Protocol.HTTP_2)
                    .message("Request not allowed")
                    .request(chain.request())
                    .body(ResponseBody.create(MediaType.parse("application/json"), "Body"))
                    .build();
        }

        violations.resolveViolations(context);

        //Here we would write the request body back into a sink and update the content of the
        //original request
        okhttp3.RequestBody newBody = parser.toOkHttpBody(context);
        Request newRequest = chain.request().newBuilder().method(chain.request().method(), newBody).build();
        return chain.proceed(newRequest);
    }
}
