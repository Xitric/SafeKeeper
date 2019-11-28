package dk.sdu.privacyenforcer.client.filters;

import dk.sdu.privacyenforcer.client.Privacy;

public abstract class AbstractFilter implements Filter {

    private Privacy.Mutation mode = Privacy.Mutation.BLOCK;

    public Privacy.Mutation getMode() {
        return mode;
    }

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
}
