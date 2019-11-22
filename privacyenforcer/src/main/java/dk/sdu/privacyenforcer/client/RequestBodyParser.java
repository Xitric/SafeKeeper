package dk.sdu.privacyenforcer.client;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okio.Buffer;

class RequestBodyParser {

    JSONObject toJson(okhttp3.RequestBody body) {
        if (body != null) {
            try (Buffer buffer = new Buffer()) {
                body.writeTo(buffer);
                return new JSONObject(buffer.readUtf8());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    okhttp3.RequestBody toHttpBody(JSONObject json) {
        return okhttp3.RequestBody.create(MediaType.parse("application/json"), json.toString());
    }
}
