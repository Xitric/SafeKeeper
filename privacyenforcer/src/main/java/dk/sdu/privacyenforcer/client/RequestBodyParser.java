package dk.sdu.privacyenforcer.client;

import java.io.IOException;
import java.util.Scanner;

import okio.Buffer;

class RequestBodyParser {

    RequestBody toInternalBody(okhttp3.RequestBody body) {
        RequestBody result = null;

        try (Buffer buffer = new Buffer()) {
            body.writeTo(buffer);
            StringBuilder bodyContents = new StringBuilder();

            try (Scanner sc = new Scanner(buffer.inputStream())) {
                while (sc.hasNextLine()) {
                    bodyContents.append(sc.nextLine());
                }

                result = new RequestBody(bodyContents.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    okhttp3.RequestBody toOkHttpBody(RequestBody body) {
        return null;
    }
}
