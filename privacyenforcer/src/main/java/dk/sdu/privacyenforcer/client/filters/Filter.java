package dk.sdu.privacyenforcer.client.filters;

import org.json.JSONObject;

import dk.sdu.privacyenforcer.client.RequestUrl;
import dk.sdu.privacyenforcer.client.ViolationCollection;
import dk.sdu.privacyenforcer.ui.Privacy;

public interface Filter {

    void setMode(Privacy.Mutation mode);

    void filter(RequestUrl requestUrl, ViolationCollection violations);

    void filter(JSONObject body, ViolationCollection violations);
}
