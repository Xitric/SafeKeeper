package dk.sdu.privacyenforcer.client.mutators;

import org.json.JSONObject;

import dk.sdu.privacyenforcer.client.RequestUrl;
import okhttp3.HttpUrl;

public class LocationKAnonymityMutator implements DataMutator {

    @Override
    public void mutate(RequestUrl requestUrl, String[] flaggedParameters) {
        //TODO: Fake position
        HttpUrl newUrl = requestUrl.getUrl().newBuilder()
                .setEncodedQueryParameter(flaggedParameters[0], "40.75115343118427")
                .setEncodedQueryParameter(flaggedParameters[1], "-73.9883633370433")
                .build();

        requestUrl.setUrl(newUrl);
    }

    @Override
    public void mutate(JSONObject[] flaggedObjects) {
        //TODO
    }
}