package dk.sdu.privacyenforcer.client.filters;

import dk.sdu.privacyenforcer.client.PrivacyViolation;
import dk.sdu.privacyenforcer.client.RequestContent;
import dk.sdu.privacyenforcer.client.ViolationCollection;
import dk.sdu.privacyenforcer.client.mutators.DataMutator;
import dk.sdu.privacyenforcer.ui.Privacy;

public class FineLocationFilter implements Filter {

    private final DataMutator mutator = new LocationKAnonymityMutator();
    private Privacy.Mutation mode = Privacy.Mutation.BLOCK;

    @Override
    public void setMode(Privacy.Mutation mode) {
        this.mode = mode;
    }

    @Override
    public void filter(RequestContent context, ViolationCollection violations) {
        if (mode == Privacy.Mutation.ALLOW) return;

        PrivacyViolation violation1 = new PrivacyViolation(context.getContent().indexOf("Hello"), context.getContent().indexOf("Hello") + "Hello".length(), context, kAnonymity);
        violations.addViolation(violation1);

        PrivacyViolation violation2 = new PrivacyViolation(context.getContent().indexOf("world"), context.getContent().indexOf("world") + "world".length(), context, localObfuscation);
        violations.addViolation(violation2);
    }

    private class LocationKAnonymityMutator implements DataMutator {

        @Override
        public void mutate(RequestContent context, PrivacyViolation violation) {
            context.substitute(violation.getBeginOffset(), violation.getEndOffset(), "Lol");
        }
    }
}
