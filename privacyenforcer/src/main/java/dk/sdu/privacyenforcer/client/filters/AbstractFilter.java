package dk.sdu.privacyenforcer.client.filters;

import dk.sdu.privacyenforcer.ui.Privacy;

public abstract class AbstractFilter implements Filter {

    private Privacy.Mutation mode = Privacy.Mutation.BLOCK;

    @Override
    public void setMode(Privacy.Mutation mode) {
        this.mode = mode;
    }

    public Privacy.Mutation getMode() {
        return mode;
    }

    public boolean shouldSkip() {
        return mode == Privacy.Mutation.ALLOW;
    }

    public boolean shouldAbort() {
        return mode == Privacy.Mutation.BLOCK;
    }
}
