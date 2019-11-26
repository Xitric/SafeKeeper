package dk.sdu.privacyenforcer.client;

import dk.sdu.privacyenforcer.client.mutators.DataMutator;

public abstract class PrivacyViolation {

    private final DataMutator mutator;

    public PrivacyViolation(DataMutator mutator) {
        this.mutator = mutator;
    }

    public DataMutator getMutator() {
        return mutator;
    }

    public abstract void resolve();
}
