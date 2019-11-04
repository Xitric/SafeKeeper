package dk.sdu.privacyenforcer.client;

import java.io.IOException;

import okhttp3.MediaType;
import okio.Buffer;

class RequestBodyParser {

    RequestBody toInternalBody(okhttp3.RequestBody body) {
        try (Buffer buffer = new Buffer()) {
            body.writeTo(buffer);
            return new RequestBody(buffer.readUtf8());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    okhttp3.RequestBody toOkHttpBody(RequestBody body) {
        return okhttp3.RequestBody.create(MediaType.parse("application/json"), body.getContent());
    }
}
