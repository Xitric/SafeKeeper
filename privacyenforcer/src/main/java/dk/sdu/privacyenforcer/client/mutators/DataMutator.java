package dk.sdu.privacyenforcer.client.mutators;

import org.json.JSONObject;

import dk.sdu.privacyenforcer.client.RequestUrl;

public interface DataMutator {

    void mutate(RequestUrl requestUrl, String[] flaggedParameters);

    void mutate(JSONObject body, String[] flaggedElements);
}
