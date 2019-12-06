package dk.sdu.privacyenforcer.client.mutators;

import org.json.JSONObject;

import dk.sdu.privacyenforcer.client.RequestUrl;

/**
 * Interface describing objects that can be used to replace privacy violating content in http
 * requests with fake counterparts. Subclasses must support replacement in both request urls and
 * request bodies.
 */
public interface DataMutator {

    void mutate(RequestUrl requestUrl, String[] flaggedParameters);

    void mutate(JSONObject body, String[] flaggedElements);
}
