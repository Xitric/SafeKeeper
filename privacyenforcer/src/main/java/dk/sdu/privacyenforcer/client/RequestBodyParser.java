package dk.sdu.privacyenforcer.client;

import java.io.IOException;

import okhttp3.MediaType;
import okio.Buffer;

class RequestBodyParser {

    RequestContent toInternalBody(okhttp3.RequestBody body) {
        if (body != null) {
            try (Buffer buffer = new Buffer()) {
                body.writeTo(buffer);
                return new RequestContent(buffer.readUtf8());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new RequestContent("");
    }

    okhttp3.RequestBody toOkHttpBody(RequestContent body) {
        return okhttp3.RequestBody.create(MediaType.parse("application/json"), body.getContent());
    }
}
