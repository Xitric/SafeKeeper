package dk.sdu.privacyenforcer.client.filters;

import org.json.JSONObject;

import java.util.Map;

import dk.sdu.privacyenforcer.client.Privacy;
import dk.sdu.privacyenforcer.client.RequestUrl;
import dk.sdu.privacyenforcer.client.ViolationCollection;
import dk.sdu.privacyenforcer.client.mutators.DataMutator;

public interface Filter {

    void setMode(Privacy.Mutation mode);

    void setDataMutator(String mutatorId);

    void filter(RequestUrl requestUrl, ViolationCollection violations);

    void filter(JSONObject body, ViolationCollection violations);

    Map<String, DataMutator> getMutators();

}
