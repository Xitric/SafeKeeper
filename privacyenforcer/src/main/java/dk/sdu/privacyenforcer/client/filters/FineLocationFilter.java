package dk.sdu.privacyenforcer.client.filters;

import org.json.JSONObject;

import dk.sdu.privacyenforcer.client.ViolationCollection;
import dk.sdu.privacyenforcer.client.mutators.DataMutator;
import okhttp3.HttpUrl;

public class FineLocationFilter extends AbstractFilter {

    private final DataMutator mutator = new LocationKAnonymityMutator();

    @Override
    public void filter(HttpUrl url, ViolationCollection violations) {
        if (shouldSkip()) return;

        //TODO
    }

    @Override
    public void filter(JSONObject body, ViolationCollection violations) {
        if (shouldSkip()) return;

        //TODO
    }

    private class LocationKAnonymityMutator implements DataMutator {

        @Override
        public void mutate(HttpUrl url, String[] flaggedParameters) {
            //TODO
        }

        @Override
        public void mutate(JSONObject[] flaggedObjects) {
            //TODO
        }
    }
}
