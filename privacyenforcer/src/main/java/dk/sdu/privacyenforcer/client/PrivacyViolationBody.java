package dk.sdu.privacyenforcer.client;

import org.json.JSONObject;

import dk.sdu.privacyenforcer.client.mutators.DataMutator;

public class PrivacyViolationBody extends PrivacyViolation {

    private JSONObject body;
    private String[] flaggedElements;

    public PrivacyViolationBody(DataMutator mutator, JSONObject body, String[] flaggedElements) {
        super(mutator);
        this.body = body;
        this.flaggedElements = flaggedElements;
    }

    @Override
    public void resolve() {
        getMutator().mutate(body, flaggedElements);
    }
}
