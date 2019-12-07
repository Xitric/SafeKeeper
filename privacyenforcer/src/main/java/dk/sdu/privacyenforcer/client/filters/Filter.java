package dk.sdu.privacyenforcer.client.filters;

import org.json.JSONObject;

import java.util.Map;

import dk.sdu.privacyenforcer.client.Privacy;
import dk.sdu.privacyenforcer.client.RequestUrl;
import dk.sdu.privacyenforcer.client.ViolationCollection;
import dk.sdu.privacyenforcer.client.mutators.DataMutator;

/**
 * Interface describing objects that can be used to detect privacy violating content in outgoing
 * http requests. Filters can be configured to control the action they take when such data is
 * detected, and whether the filter should be ignored.
 */
public interface Filter {

    void setMode(Privacy.Mutation mode);

    Map<String, DataMutator> getMutators();

    void setDataMutator(String mutatorId);

    void filter(RequestUrl requestUrl, ViolationCollection violations);

    void filter(JSONObject body, ViolationCollection violations);
}
