package dk.sdu.privacyenforcer.client;

import dk.sdu.privacyenforcer.client.mutators.DataMutator;
import okhttp3.HttpUrl;

public class PrivacyViolationUrl extends PrivacyViolation {

    private HttpUrl url;
    private String[] flaggedParameters;

    public PrivacyViolationUrl(DataMutator mutator, HttpUrl url, String[] flaggedParameters) {
        super(mutator);
        this.url = url;
        this.flaggedParameters = flaggedParameters;
    }

    @Override
    public void resolve() {
        getMutator().mutate(url, flaggedParameters);
    }
}
