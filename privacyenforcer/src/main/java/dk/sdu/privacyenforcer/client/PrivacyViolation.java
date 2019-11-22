package dk.sdu.privacyenforcer.client;

import dk.sdu.privacyenforcer.client.mutators.DataMutator;

public class PrivacyViolation {

    private final int beginOffset;
    private final int endOffset;
    private final RequestContent context;
    private final DataMutator mutator;

    public PrivacyViolation(int beginOffset, int endOffset, RequestContent context, DataMutator mutator) {
        this.beginOffset = beginOffset;
        this.endOffset = endOffset;
        this.context = context;
        this.mutator = mutator;
    }

    public int getBeginOffset() {
        return beginOffset;
    }

    public int getEndOffset() {
        return endOffset;
    }

    public String getContent() {
        return context.getContent(this.getBeginOffset(), this.getEndOffset());
    }

    public void resolve() {
        mutator.mutate(context, this);
    }
}
