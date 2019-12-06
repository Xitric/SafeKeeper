package dk.sdu.privacyenforcer.client.filters;

import java.util.HashMap;
import java.util.Map;

import dk.sdu.privacyenforcer.client.Privacy;
import dk.sdu.privacyenforcer.client.mutators.DataMutator;

/**
 * Convenience implementation of the {@link Filter} interface allowing subclasses to store available
 * mutators and easily interpret their filtering modes.
 */
public abstract class AbstractFilter implements Filter {

    protected Privacy.Mutation mode = Privacy.Mutation.BLOCK;
    protected DataMutator mutator;
    protected Map<String, DataMutator> mutators = new HashMap<>();

    @Override
    public void setMode(Privacy.Mutation mode) {
        this.mode = mode;
    }

    public boolean shouldSkip() {
        return mode == Privacy.Mutation.ALLOW;
    }

    public boolean shouldAbort() {
        return mode == Privacy.Mutation.BLOCK;
    }

    @Override
    public Map<String, DataMutator> getMutators() {
        return mutators;
    }

    @Override
    public void setDataMutator(String mutatorId) {
        if (mutatorId != null) {
            this.mutator = mutators.get(mutatorId);
        }
    }
}
