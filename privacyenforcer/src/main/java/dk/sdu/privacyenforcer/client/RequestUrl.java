package dk.sdu.privacyenforcer.client;

import okhttp3.HttpUrl;

public class RequestUrl {

    private HttpUrl url;

    public RequestUrl(HttpUrl url) {
        this.url = url;
    }

    public HttpUrl getUrl() {
        return url;
    }

    public void setUrl(HttpUrl url) {
        this.url = url;
    }
}
