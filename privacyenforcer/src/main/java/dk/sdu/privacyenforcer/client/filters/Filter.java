package dk.sdu.privacyenforcer.client.filters;

import org.json.JSONObject;

import java.util.ArrayList;

import dk.sdu.privacyenforcer.client.RequestUrl;
import dk.sdu.privacyenforcer.client.ViolationCollection;
import dk.sdu.privacyenforcer.client.Privacy;

public interface Filter {

    void setMode(Privacy.Mutation mode);

    void filter(RequestUrl requestUrl, ViolationCollection violations);

    void filter(JSONObject body, ViolationCollection violations);

    ArrayList<String> getMutatorIdentifiers();
}
