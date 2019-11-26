package dk.sdu.privacyenforcer.client;

import org.json.JSONObject;

import dk.sdu.privacyenforcer.client.mutators.DataMutator;

public class PrivacyViolationBody extends PrivacyViolation {

    private JSONObject[] flaggedObjects;

    public PrivacyViolationBody(DataMutator mutator, JSONObject[] flaggedObjects) {
        super(mutator);
        this.flaggedObjects = flaggedObjects;
    }

    @Override
    public void resolve() {
        getMutator().mutate(flaggedObjects);
    }
}
