package dk.sdu.privacyenforcer.client.filters;

import org.json.JSONObject;

import dk.sdu.privacyenforcer.client.ViolationCollection;
import dk.sdu.privacyenforcer.ui.Privacy;
import okhttp3.HttpUrl;

public interface Filter {

    void setMode(Privacy.Mutation mode);

    void filter(HttpUrl url, ViolationCollection violations);

    void filter(JSONObject body, ViolationCollection violations);
}
