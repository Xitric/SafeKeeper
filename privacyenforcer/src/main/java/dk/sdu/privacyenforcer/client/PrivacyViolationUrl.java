package dk.sdu.privacyenforcer.client;

import dk.sdu.privacyenforcer.client.mutators.DataMutator;

public class PrivacyViolationUrl extends PrivacyViolation {

    private RequestUrl url;
    private String[] flaggedParameters;

    public PrivacyViolationUrl(DataMutator mutator, RequestUrl url, String[] flaggedParameters) {
        super(mutator);
        this.url = url;
        this.flaggedParameters = flaggedParameters;
    }

    @Override
    public void resolve() {
        getMutator().mutate(url, flaggedParameters);
    }
}
