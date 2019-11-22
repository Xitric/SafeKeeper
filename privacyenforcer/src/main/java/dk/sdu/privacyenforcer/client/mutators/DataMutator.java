package dk.sdu.privacyenforcer.client.mutators;

import org.json.JSONObject;

import okhttp3.HttpUrl;

public interface DataMutator {

    void mutate(HttpUrl url, String[] flaggedParameters);

    void mutate(JSONObject[] flaggedObjects);
}
